package spark;
import utile._
import org.apache.spark.sql.SparkSession
import org.apache.spark.SparkContext
import org.apache.spark.SparkConf
import org.apache.spark.rdd.RDD
import models._
import scala.collection.mutable.ArrayBuffer
import scala.math._
import org.apache.spark.storage._
import java.time.LocalTime
import java.time.temporal.ChronoUnit
import util.control.Breaks._



class AnalisiDimensionale(sparkContext : SparkContext){
    
    val fileTaxi=(sys.env.get("DATASET").get.replaceAll("\\\\", "/"))+"/dataset/dataset.txt"
    val rdd = sparkContext.textFile(fileTaxi).map(x => Util.parseRilevazione(x)).sample(false, 0.01)
    //.filter(x => x.id <= 25)
    //quetse dimensioni sono ordinate lessicograficamente
    val dimensioni=Array("data", "latitudine", "longitudine", "ora")
    val numeri=Map(
    "latitudine" -> (44.0,35.0),
    "longitudine" -> (118.0,115.0)
    )
    val tempo=Map("ora" -> ("23:59:59","00:00:00"))

    def queryDimensionale(dimensioniUtente : List[String]): Array[(List[Object], Int)] = { // da vedere
        println("chiamato metodo")
        val dimensioniUtenteRangeOrdinate=dimensioniRangeOrdinati(dimensioniUtente)
        val dimensioniUtenteOrdinate=dimensioniUtenteRangeOrdinate.map(x => x._1)
        val rangeUtenteOrdinati=dimensioniUtenteRangeOrdinate.map(x => x._2)
        for(numero <- 1 until ((pow(2,dimensioni.length)).asInstanceOf[Int]) ) {
            val stringaBit1 = (numero).toBinaryString
            var stringaBit = stringaBit1
            if(stringaBit1.length < 4){
                var zeri=""
                for(i <- 1 to 4-stringaBit1.length) zeri=s"0$zeri"
                stringaBit=s"$zeri$stringaBit1"
            }
            var sottoinsiemeDimensioniQuery = new ArrayBuffer[String]()
            for(indice <- 0 until stringaBit.length){
                if(stringaBit.charAt(indice)=='1'){
                    sottoinsiemeDimensioniQuery += this.dimensioni(indice)
                }
            }
            if(sottoinsiemeDimensioniQuery sameElements dimensioniUtenteOrdinate){
                println(s"beccate dimensioni: $sottoinsiemeDimensioniQuery---$dimensioniUtenteOrdinate")
                val numeroDimensioni=sottoinsiemeDimensioniQuery.length
                //x.getClass.getDeclaredMethod(dim).invoke(x) = x.dim
                //[x,y)
                val dim0 = sottoinsiemeDimensioniQuery(0)
                val ampiezza0 = rangeUtenteOrdinati(0)
                if(ampiezza0 == -1) return rdd.map(x => (List( x.getClass.getDeclaredMethod(dim0).invoke(x) ) , 1)).reduceByKey(_+_).collect
                else {
                    var max=null : Bound; var min=null : Bound;
                    var tempu=false
                    if(tempo.contains(dim0)){
                        max=new Bound(tempo(dim0)._1)
                        min=new Bound(tempo(dim0)._2) 
                        tempu=true              
                    }
                    else{
                        max=new Bound(numeri(dim0)._1)
                        min=new Bound(numeri(dim0)._2)
                    }
                    var lb=new Bound(min.valore) 
                    var ub=lb+ampiezza0
                    var vettore = new ArrayBuffer[(List[Object], Int)]();
                    rdd.cache()
                    var incontratoMezzanotte=0
                    breakable {
                        while(lb < max){
                            if(tempu){
                                if(LocalTime.parse(lb.valoreTempo).getHour()==0)
                                    incontratoMezzanotte=incontratoMezzanotte+1
                                if(incontratoMezzanotte==2) break
                            }
                            println(s"lb=$lb   ub=$ub   max=$max")
                            var categoria0 = s"da $lb a $ub"
                            val array=rdd.filter(x =>
                                new Bound(x.getClass.getDeclaredMethod(dim0).invoke(x)) >= lb &&
                                new Bound(x.getClass.getDeclaredMethod(dim0).invoke(x)) < ub
                            ).map(x => (List( categoria0 ) , 1)).reduceByKey(_+_).collect
                            if(array.length!=0) vettore += (array(0))
                            else vettore+=((List(categoria0), 0))
                            lb=ub
                            ub=ub+ampiezza0
                        }
                    }                  
                    return vettore.toArray
                }
            }
        }
        //fittizio
        return (Array( (List(1,2,3),1), (List(4,5,6),13) )).asInstanceOf[Array[(List[Object], Int)]] 
    }



    def dimensioniRangeOrdinati(input : List[String]) : ArrayBuffer[(String, Double)]={
        var array=new ArrayBuffer[(String,Double)]
        for(i <- 0 until input.length-1){
            if(i % 2 == 0){
                array += ( (input(i),input(i+1).toDouble) )
            }
        }
        val ris=array.sortBy(x => x._1)
        println(ris)
        return ris
    }



    def maxMinDimensione(dim : String) : (Bound, Bound) = {
        var max=null : Bound; var min=null : Bound;
        if(tempo.contains(dim)){
            max=new Bound(tempo(dim)._1)
            min=new Bound(tempo(dim)._2)               
        }
        else{
            max=new Bound(numeri(dim)._1)
            min=new Bound(numeri(dim)._2)
        }
        return (max,min)
    }


/*
    def costruisciFiltro(dim : ArrayBuffer[String], range : ArrayBuffer[String]) : Rilevazione => Boolean = {
        var max = new ArrayBuffer[Bound]()
        var min = new ArrayBuffer[Bound]()
        var lb = new ArrayBuffer[Bound]()
        var ub = new ArrayBuffer[Bound]()
        var valoriDaVerificare = new ArrayBuffer[Rilevazione => Bound]()
        var condizioni = new ArrayBuffer[(Bound, Bound) => Boolean]()
        //massimi e minimi
        for(i <- 0 until dim.length){
            val dimensione=dim(i)
            if(tempo.contains(dimensione)){
                max+=new Bound(tempo(dim0)._1)
                min+=new Bound(tempo(dim0)._2)               
            }
            else{
                max+=new Bound(numeri(dim0)._1)
                min+=new Bound(numeri(dim0)._2)
            }
        }
        //upper bound e lower bound
        for(i <- 0 until range.length){
            val ampiezza=range(i)
            if(ampiezza==-1){
                lb+=new Bound(-1) 
                ub+=new Bound(-1)             
            }
            else{
                lb+=min(i)
                ub+=(lb+ampiezza)
            }
        }
        //valori da mettere nella condizione
        for(i <- 0 until dim.length){
            val dimensione=dim(i)
            valoriDaVerificare+= x : Rilevazione => new Bound( x.getClass.getDeclaredMethod(dimensione).invoke(x) )
        }
        for(i <- 0 until range.length){
            val lbi=lb(i)
            val ubi=ub(i)
            if(range(i)==-1){
                condizioni+=(lowerBound: Bound, upperBound : Bound) => true         
            }
            else{
                condizioni+=(lowerBound: Bound, upperBound : Bound) => valoriDaVerificare(i) >= lbi && valoriDaVerificare(i) < ubi
            }
        }
    }
*/

   

}
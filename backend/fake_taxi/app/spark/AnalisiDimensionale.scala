package spark;
import utile._
import org.apache.spark.sql.SparkSession
import org.apache.spark.SparkContext
import org.apache.spark.SparkConf
import org.apache.spark.rdd.RDD
import models._
import scala.collection.mutable.ArrayBuffer
import scala.math._



class AnalisiDimensionale(sparkContext : SparkContext){
    
    val fileTaxi=(sys.env.get("DATASET").get.replaceAll("\\\\", "/"))+"/dataset/dataset.txt"
    val rdd = sparkContext.textFile(fileTaxi).map(x => Util.parseRilevazione(x))
    //quetse dimensioni sono ordinate lessicograficamente
    val dimensioni=Array("data", "latitudine", "longitudine", "ora")
    val numeri=Map(
    "latitudine" -> rdd.map(x => x.latitudine),
    "longitudine" -> rdd.map(x => x.longitudine)
    )
    val tempo=Map("ora" -> rdd.map(x => x.ora))

    def queryDimensionale(dimensioniUtente : List[String]): Array[(List[Object], Int)] = { // da vedere
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
                if(numeroDimensioni==1){
                    println("numero dimensioni=1")
                    val dim0 = sottoinsiemeDimensioniQuery(0)
                    val ampiezza0 = rangeUtenteOrdinati(0)
                    if(ampiezza0 == -1) return rdd.map(x => (List( x.getClass.getDeclaredMethod(dim0).invoke(x) ) , 1)).reduceByKey(_+_).collect
                    else {
                        println("ciao")
                        var max=null : Bound; var min=null : Bound;
                        if(tempo.contains(dim0)){
                            println("miao")
                            max=new Bound(tempo(dim0).max)
                            println("miao eccolo")
                            min=new Bound(tempo(dim0).min)               
                        }
                        else{
                            println("bau")
                            max=new Bound(numeri(dim0).max)
                            min=new Bound(numeri(dim0).min)
                        }
                        var lb=new Bound(min.valore) 
                        var ub=lb+ampiezza0
                        var vettore = new ArrayBuffer[(List[Object], Int)]();
                        while(lb < max){
                            println(s"lb=$lb   ub=$ub   max=$max")
                            var categoria0 = s"[$lb,$ub)"
                            val array=rdd.filter(x =>
                                new Bound(x.getClass.getDeclaredMethod(dim0).invoke(x)) >= lb &&
                                new Bound(x.getClass.getDeclaredMethod(dim0).invoke(x)) < ub
                            ).map(x => (List( categoria0 ) , 1)).reduceByKey(_+_).collect
                            vettore += (array(0))
                            lb=ub
                            ub=ub+ampiezza0
                        }
                        println("ritorna cazzo ritorna")
                        return vettore.toArray
                    }
                }
                else if(numeroDimensioni==2){
                    val dim0 = sottoinsiemeDimensioniQuery(0)
                    val dim1= sottoinsiemeDimensioniQuery(1)
                    return rdd.map(x => (List( x.getClass.getDeclaredMethod(dim0).invoke(x), x.getClass.getDeclaredMethod(dim1).invoke(x) ) , 1)).reduceByKey(_+_).collect                
                }
                //else if(numeroDimensioni==3)
                //else if(numeroDimensioni==4)
                //else if(numeroDimensioni==5)
                else{
                    //fittizio
                    return (Array( (List(1,2,3),1), (List(4,5,6),13) )).asInstanceOf[Array[(List[Object], Int)]]
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


   

}
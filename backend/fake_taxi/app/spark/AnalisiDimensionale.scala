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

    def queryDimensionale(dimensioniUtente : List[String]): Array[(List[Object], Int)] = { // da vedere
        val dimensioniUtenteRangeOrdinate=dimensioniRangeOrdinati(dimensioniUtente)
        val dimensioniUtenteOrdinate=dimensioniUtenteRangeOrdinate.map(x => x._1)
        val rangeUtenteOrdinati=dimensioniUtenteOrdinate.map(x => x._2)
        for(numero <- 1 until ((pow(2,dimensioni.length)).asInstanceOf[Int]) ) {
            val stringaBit = (numero).toBinaryString
            var sottoinsiemeDimensioniQuery = new ArrayBuffer[String]()
            for(indice <- 0 until stringaBit.length){
                if(stringaBit.charAt(indice)=='1'){
                    sottoinsiemeDimensioniQuery += this.dimensioni(indice)
                }
            }
            if(sottoinsiemeDimensioniQuery sameElements dimensioniUtenteOrdinate){
                val numeroDimensioni=sottoinsiemeDimensioniQuery.length
                //x.getClass.getDeclaredMethod(dim).invoke(x) = x.dim
                //[x,y)
                if(numeroDimensioni==1){
                    val dim0 = sottoinsiemeDimensioniQuery(0)
                    val ampiezza0 = rangeUtenteOrdinati(0)
                    if(ampiezza0 == -1) return rdd.map(x => (List( x.getClass.getDeclaredMethod(dim0).invoke(x) ) , 1)).reduceByKey(_+_).collect
                    else {
                        val max=rdd.max(x.getClass.getDeclaredMethod(dim0).invoke(x)))
                        val min=rdd.min(x.getClass.getDeclaredMethod(dim0).invoke(x)))
                        var lb=min
                        var ub=lb+ampiezza0
                        var vettore = new ArrayBuffer[(List[Object], Int)]();
                        while(lb < max){
                            var categoria0 = s"[$lb,$ub)"
                            val array=rdd.filter(
                                x.getClass.getDeclaredMethod(dim0).invoke(x) >= lb &&
                                x.getClass.getDeclaredMethod(dim0).invoke(x) < ub
                            )
                            .map(x => (List( categoria0 ) , 1)).reduceByKey(_+_).collect
                            vettore += (array(0))
                            lb=ub
                            ub=ub+ampiezza0
                        }
                    }
                }
                else if(numeroDimensioni==2){
                    val dim0 = sottoinsiemeDimensioniQuery(0)
                    val dim1= sottoinsiemeDimensioniQuery(1)
                    return rdd.map(x => (List( x.getClass.getDeclaredMethod(dim0).invoke(x), x.getClass.getDeclaredMethod(dim1).invoke(x) ) , 1)).reduceByKey(_+_).collect                }
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
        var array=new ArrayBuffer[(String,String)]
        for(i <- 0 until input.length-1){
            if(i % 2 == 0){
                array += ( (input(i),input(i+1).asInstanceOf[Double]) )
            }
        }
        array.sortBy(x => x._1);
    }


   

}
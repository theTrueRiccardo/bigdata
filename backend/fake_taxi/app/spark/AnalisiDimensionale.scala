package spark;
import utile._
import org.apache.spark.sql.SparkSession
import org.apache.spark.SparkContext
import org.apache.spark.SparkConf
import org.apache.spark.rdd.RDD
import models._
import scala.collection.mutable.ArrayBuffer


class AnalisiDimensionale(sparkContext : SparkContext){
    //quetse dimensioni sono ordinate lessicograficamente
    val dimensioni=Array("data", "latitudine", "longitudine", "ora", "taxi")
    //l'asterisco indica un vararg, non una simpatizzazione verso la comunità LGBT
    def queryDimensionale(rdd : RDD[Rilevazione],dimensioni : Seq[String]): Array[(Object, Int)] ={ // da vedere
        val dimensioniOrdinate=dimensioni.sorted;// da vedere
        //for(numero : Int <- 1 to 31) {
            val numero = 0 // da togliere
            val stringaBit = (numero).toBinaryString
            var sottoinsiemeDimensioniQuery = new ArrayBuffer[String]()
            for(indice <- 0 until stringaBit.length){
                if(stringaBit.charAt(indice)=='1'){
                    sottoinsiemeDimensioniQuery += this.dimensioni(indice)
                }
            }
            val numeroDimensioni=sottoinsiemeDimensioniQuery.length
            //if(numeroDimensioni==1){
            val dim0 = sottoinsiemeDimensioniQuery(0)
            return rdd.map(x => (x.getClass.getDeclaredMethod(dim0).invoke(x) , 1)).reduceByKey(_+_).collect
            //}
            //if(numeroDimensioni==1) 
            //else if(numeroDimensioni==2)
            //else if(numeroDimensioni==3)
            //else if(numeroDimensioni==4)
            //else if(numeroDimensioni==5)
       // }       
    }
}
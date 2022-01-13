package spark;
import org.apache.spark.sql.SparkSession
import org.apache.spark.SparkContext
import org.apache.spark.SparkConf
import models._
import utile._

class Dao{
    
    val SPARK_HOME=sys.env.get("SPARK_HOME").get.replaceAll("\\\\", "/")
    val dataset = sys.env.get("DATASET").get.replaceAll("\\\\", "/") // Should be some file on your system
    val spark = SparkSession.builder
            .appName("Simple Application")
            .config("spark.master", "local")
            .getOrCreate()
    //val sparkContext = new SparkContext()
    val logData  = spark.sparkContext.textFile("C:/Users/Riccardo/Desktop/taxi_log_2008_by_id/1.txt").cache()
    //val utile=new Util()
    //val spark = SparkSession.builder.appName("Simple Application").getOrCreate()
    //val logData = spark.read.textFile(logFile).cache()
    //val numAs = logData.filter(line => line.contains("a")).count()
    //val numBs = logData.filter(line => line.contains("b")).count()
    //println(s"Lines with a: $numAs, Lines with b: $numBs") 's' iniziale carattere speciale
    //spark.stop()

   
    

    def prendiTutti() : Array[Taxi] ={
        return logData.map(x => Util.parseTaxi(x)).collect().toArray.take(10)
        //la seguente query prende tutti i taxi che sono alla latitudine 39.93883
    }

    def mattia() : (Coordinata, Int) ={        
        val taxi = logData.map(x => Util.parseTaxi(x))
        val result = taxi.map(x => (x.coordinata, 1),[1,5,6,3,2,15,32]).reduceByKey(_+_).collect.maxBy(_._2)
        return result
    }
    
    
    def query() : String = {
        val numAs = logData.filter(line => line.contains("a")).count()
        val numBs = logData.filter(line => line.contains("b")).count()
        return s"Lines with a: $numAs, Lines with b: $numBs"
    }
}
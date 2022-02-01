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
            .config("spark.master", "local[*]")
            .getOrCreate()
    //val sparkContext = new SparkContext()
    val analisiPerTaxi=new AnalisiPerTaxi(spark.sparkContext)
    val analisiDimensionale=new AnalisiDimensionale(spark.sparkContext)
    val machineLearning=new MachineLearning(spark.sparkContext, spark, analisiPerTaxi)
    val logData = spark.sparkContext.textFile(dataset).cache()
    //val utile=new Util()
    //val spark = SparkSession.builder.appName("Simple Application").getOrCreate()
    //val logData = spark.read.textFile(logFile).cache()
    //val numAs = logData.filter(line => line.contains("a")).count()
    //val numBs = logData.filter(line => line.contains("b")).count()
    //println(s"Lines with a: $numAs, Lines with b: $numBs") 's' iniziale carattere speciale
    //spark.stop()

    

    def schedaTaxi(id : Int) : SchedaTaxi={
        return this.analisiPerTaxi.scheda(id)
    }

    def analisiDimensionale(dimensioni : List[String]) : Array[(List[Object], Int)]  ={
        return this.analisiDimensionale.queryDimensionale(dimensioni)
    }

    def pipeline() : Metriche = {
        return this.machineLearning.pipeline()
    }

    def classifica(longitudine : Double, latitudine : Double) : Int = {
        return this.machineLearning.classifica(longitudine,latitudine)
    }

    def testML(): List[String] ={
        return this.machineLearning.testML()
    }

    def cluster() = {
        this.machineLearning.cluster()
    }
}
package spark;
import org.apache.spark.sql.SparkSession

class Dao{
    
    val SPARK_HOME=sys.env.get("SPARK_HOME").get.replaceAll("\\\\", "/")
    val logFile = SPARK_HOME+"/README.md" // Should be some file on your system
    val spark = SparkSession.builder
            .appName("Simple Application")
            .config("spark.master", "local")
            .getOrCreate()
    //val spark = SparkSession.builder.appName("Simple Application").getOrCreate()
    //val logData = spark.read.textFile(logFile).cache()
    //val numAs = logData.filter(line => line.contains("a")).count()
    //val numBs = logData.filter(line => line.contains("b")).count()
    //println(s"Lines with a: $numAs, Lines with b: $numBs") 's' iniziale carattere speciale
    //spark.stop()
    
    def query() : String = {
        val logData = spark.read.textFile(logFile).cache()
        val numAs = logData.filter(line => line.contains("a")).count()
        val numBs = logData.filter(line => line.contains("b")).count()
        return s"Lines with a: $numAs, Lines with b: $numBs"
    }
}
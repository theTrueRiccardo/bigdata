package spark;
import org.apache.spark.sql.SparkSession
import org.apache.spark.SparkContext
import org.apache.spark.SparkConf
import models._
import utile._

class Dao {
  val dataset = sys.env
    .get("DATASET")
    .get
    .replaceAll("\\\\", "/") // Should be some file on your system
  val spark = SparkSession.builder
    .appName("Simple Application")
    .config("spark.master", "local[*]")
    .getOrCreate()
  val analisiPerTaxi = new AnalisiPerTaxi(spark.sparkContext)
  val analisiDimensionale = new AnalisiDimensionale(spark.sparkContext)
  val machineLearning =
    new MachineLearning(spark.sparkContext, spark, analisiPerTaxi)
  val logData = spark.sparkContext.textFile(dataset).cache()

  def schedaTaxi(id: Int): SchedaTaxi = {
    return this.analisiPerTaxi.scheda(id)
  }
  def valori() {
    this.analisiPerTaxi.valori
  }

  def analisiDimensionale(
      dimensioni: List[String]
  ): Array[(List[Object], Int)] = {
    return this.analisiDimensionale.queryDimensionale(dimensioni)
  }

  def pipeline(): Metriche = {
    return this.machineLearning.pipeline()
  }

  def classifica(longitudine: Double, latitudine: Double): Int = {
    return this.machineLearning.classifica(longitudine, latitudine)
  }

  def testML(): List[String] = {
    return this.machineLearning.testML()
  }

  def cluster(): String = {
    return this.machineLearning.cluster()
  }
}

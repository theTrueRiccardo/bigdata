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
    val machineLearning=new MachineLearning(spark.sparkContext)
    val logData = spark.sparkContext.textFile(dataset).cache()
    //val utile=new Util()
    //val spark = SparkSession.builder.appName("Simple Application").getOrCreate()
    //val logData = spark.read.textFile(logFile).cache()
    //val numAs = logData.filter(line => line.contains("a")).count()
    //val numBs = logData.filter(line => line.contains("b")).count()
    //println(s"Lines with a: $numAs, Lines with b: $numBs") 's' iniziale carattere speciale
    //spark.stop()

   
    

    def prendiTutti() : Array[Rilevazione] ={
        return logData.map(x => Util.parseRilevazione(x)).collect().toArray.take(10)
        //la seguente query prende tutti i taxi che sono alla latitudine 39.93883
    }

    def mattia() : (Coordinata, Int) ={        
        val taxi = logData.map(x => Util.parseRilevazione(x))
       
        val result = taxi.map(x => (x.coordinata, 1)).reduceByKey(_+_).collect.maxBy(_._2)
        return result
    }
    
    def orario(orarioInizio : Double, orarioFine: Double) : Array[(Int,Int)] ={        
        val taxi = logData.map(x => Util.parseRilevazione(x))        
        val result = taxi.filter(x => x.dataOra.ore < orarioFine && x.dataOra.ore > orarioInizio).map(x => (x.id, 1)).reduceByKey(_+_).collect
        return result
    }

    def area(inizioLong : Double, fineLong: Double , inizioLat : Double, fineLat: Double) : Array[(Int,Int)] ={        
        val taxi = logData.map(x => Util.parseRilevazione(x))       
        val result = taxi.filter(x => x.coordinata.longitudine <= fineLong && x.coordinata.longitudine >= inizioLong && x.coordinata.latitudine <= fineLat && x.coordinata.latitudine >= inizioLat )
        .map(x => (x.id, 1))
        .reduceByKey(_+_)
        .collect
        return result
    }

    def confrontoGiornoNotte(id : Int) : String ={       
        val taxi = logData.map(x => Util.parseRilevazione(x))
        val totale = taxi.count
        val giorno = taxi.filter(x => x.id == id && x.dataOra.ore >= 7 && x.dataOra.ore < 20).count
        val notte = totale - giorno
        if (giorno > notte ) return s"il taxi con id: $id lavora principalmente di giorno; giorno: $giorno, notte: $notte."   
        s"il taxi con id: $id lavora principalmente di notte; giorno: $giorno, notte: $notte."
    
    }
    
    def query() : String = {
        val numAs = logData.filter(line => line.contains("a")).count()
        val numBs = logData.filter(line => line.contains("b")).count()
        return s"Lines with a: $numAs, Lines with b: $numBs"
    }

    

    def schedaTaxi(id : Int) : SchedaTaxi={
        return analisiPerTaxi.scheda(id)
    }

    def analisiDimensionale(dimensioni : List[String]) : Array[(List[Object], Int)]  ={
        return this.analisiDimensionale.queryDimensionale(dimensioni)
    }

    def testML(): List[String] ={
        return this.machineLearning.testML()
    }
}
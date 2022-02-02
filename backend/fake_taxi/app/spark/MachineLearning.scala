package spark
import org.apache.spark.sql.SparkSession
import org.apache.spark.SparkContext
import org.apache.spark.SparkConf
import org.apache.spark.rdd.RDD
import org.apache.spark._
import scala.math._
import org.apache.spark.ml.classification.LogisticRegression
import org.apache.spark.ml.linalg.{Vector, Vectors}
import org.apache.spark.ml.param.ParamMap
import org.apache.spark.sql.Row
import org.apache.spark.ml.util._
import utile._
import models._
import org.apache.spark.sql.{Row, SparkSession}
import org.apache.spark.sql.types.{DoubleType, StringType, StructField, StructType}
import org.apache.spark.sql.functions.{when, _}
import org.apache.spark.ml.classification.MultilayerPerceptronClassifier
import org.apache.spark.ml.evaluation.MulticlassClassificationEvaluator
import org.apache.spark.ml.feature.{LabeledPoint, StandardScaler}
import org.apache.spark.ml.feature.VectorAssembler
import org.apache.spark.ml.classification.LogisticRegressionModel

import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.ml.clustering.KMeans
import org.apache.spark.ml.evaluation.ClusteringEvaluator



class MachineLearning(sparkContext : SparkContext, sparkSession : SparkSession, analisiPerTaxi : AnalisiPerTaxi){

    val fileTaxi=(sys.env.get("DATASET").get.replaceAll("\\\\", "/"))+"/dataset/dataset.txt"
    val rdd = sparkContext.textFile(fileTaxi).map(x => Util.parseRilevazione(x))


    def  testML() : List[String] ={
        
      val spark = SparkSession
      .builder
      .appName("EstimatorTransformerParamExample")
      .getOrCreate()

    
      val training = spark.createDataFrame(Seq(
      (1.0, ml.linalg.Vectors.dense(0.0, 1.1, 0.1)),
      (0.0, ml.linalg.Vectors.dense(2.0, 1.0, -1.0)),
      (0.0, ml.linalg.Vectors.dense(2.0, 1.3, 1.0)),
      (1.0, ml.linalg.Vectors.dense(0.0, 1.2, -0.5))
       )).toDF("label", "features")

        // Create a LogisticRegression instance. This instance is an Estimator.
        val lr = new LogisticRegression()
        // Print out the parameters, documentation, and any default values.
        println(s"LogisticRegression parameters:\n ${lr.explainParams()}\n")

        // We may set parameters using setter methods.
        lr.setMaxIter(10)
        .setRegParam(0.01)

        // Learn a LogisticRegression model. This uses the parameters stored in lr.
        val model1 = lr.fit(training)
        // Since model1 is a Model (i.e., a Transformer produced by an Estimator),
        // we can view the parameters it used during fit().
        // This prints the parameter (name: value) pairs, where names are unique IDs for this
        // LogisticRegression instance.
        println(s"Model 1 was fit using parameters: ${model1.parent.extractParamMap}")

        // We may alternatively specify parameters using a ParamMap,
        // which supports several methods for specifying parameters.
        val paramMap = ParamMap(lr.maxIter -> 20)
        .put(lr.maxIter, 30)  // Specify 1 Param. This overwrites the original maxIter.
        .put(lr.regParam -> 0.1, lr.threshold -> 0.55)  // Specify multiple Params.

        // One can also combine ParamMaps.
        val paramMap2 = ParamMap(lr.probabilityCol -> "myProbability")  // Change output column name.
        val paramMapCombined = paramMap ++ paramMap2

        // Now learn a new model using the paramMapCombined parameters.
        // paramMapCombined overrides all parameters set earlier via lr.set* methods.
        val model2 = lr.fit(training, paramMapCombined)
        println(s"Model 2 was fit using parameters: ${model2.parent.extractParamMap}")

        // Prepare test data.
        val test = spark.createDataFrame(Seq(
        (1.0, ml.linalg.Vectors.dense(-1.0, 1.5, 1.3)),
        (0.0, ml.linalg.Vectors.dense(3.0, 2.0, -0.1)),
        (1.0, ml.linalg.Vectors.dense(0.0, 2.2, -1.5))
        )).toDF("label", "features")
        
        var result : List[String] = List()
        
        // Make predictions on test data using the Transformer.transform() method.
        // LogisticRegression.transform will only use the 'features' column.
        // Note that model2.transform() outputs a 'myProbability' column instead of the usual
        // 'probability' column since we renamed the lr.probabilityCol parameter previously.
        model2.transform(test)
        .select("features", "label", "myProbability", "prediction")
        .collect()
        .foreach { case Row(features: Vector, label: Double, prob: Vector, prediction: Double) =>
            result = (s"(${features}, ${label}) -> prob=${prob}, prediction=${prediction}") :: result
            }
            
        return result
    }



    def pipeline() : Metriche = {
        val spark = SparkSession
        .builder
        .appName("ML")
        .config("spark.master", "local[*]")
        .getOrCreate()
        //eventualmente modificabili in 1 e 0
        var rdd1 = rdd.filter(x => x.latitudine >= 35.0 && x.latitudine <= 44.0
         && x.longitudine>=115 && x.longitudine<=118).map(x => (x.dataOrario.toLocalTime().toString(), x.longitudine, x.latitudine))
        //mettiamo il seme a pi greco per questioni di riproducibilità
        //campioniamo perchè sennò l'addestramento è temporalmente insostenibile
        //withReplacement, fraction, seed
        val rdd2=rdd1.sample(false,0.01)
        //creiamo il data frame
        val df=spark.createDataFrame(rdd2).toDF("orario","longitudine","latitudine")
        //label
        val df1=df.withColumn("label",
        when(col("orario") >="08:00:00" && col("orario") <= "23:59:59",
        lit("1").cast("int"))
        .otherwise(lit("0").cast("int")))
        val df2=df1.drop("orario")
        val assembler = new VectorAssembler()
        .setInputCols(Array("longitudine", "latitudine"))
        .setOutputCol("features")
        val df3 = assembler.transform(df2).drop("longitudine").drop("latitudine")
        //salviamo i dati per la visualizzazione offline
        //il file sta nella root del progetto ma non viene passato su git, ognuno se lo crea per se
        //println("ciao")
        //df1.write.format("csv").save("C:\\Users\\Riccardo\\Desktop\\vis.csv")
        //df.write.csv("../../vis.csv")
        /*
        df1.foreach { row => 
           row.toSeq.foreach{col => println(col) }
        }
        */
        //df1.rdd.saveAsTextFile("C:\\Users\\Riccardo\\Desktop\\vis.csv")
        /*
        var i=0;
        for (row <- df1.rdd.collect){ 
            i=i+1  
            var orario = row.mkString(",").split(",")(0)
            var longitudine = row.mkString(",").split(",")(1)
            var latitudine = row.mkString(",").split(",")(2)
            var classe = row.mkString(",").split(",")(3)
            if(i<20) println(s"$orario  $longitudine  $latitudine  $classe")   
        }
        */
        val data=df3
        println(df3.schema)
        val splits = data.randomSplit(Array(0.70, 0.30), seed = 1234L)
        val train = splits(0)
        val test = splits(1)

        //2 nodi di input, 2 livelli hidden da 2 nodi ciascuno, 2 nodi di output
        val layers = Array[Int](2, 2, 2, 2)

        
        
        
        
        
        val trainer= new LogisticRegression()
        .setFeaturesCol("features")
        .setLabelCol("label")
        
        

        // train the model
        val model = trainer.fit(train)
        model.write.overwrite.save("modello")
        // compute accuracy on the test set
        val result = model.transform(test)
        val predictionAndLabels = result.select("prediction", "label")
        val evaluator = new MulticlassClassificationEvaluator()
        .setMetricName("accuracy")
        val acc=evaluator.evaluate(predictionAndLabels)
        Metriche(acc,0,0) //fittizio
    }




    def classifica(longitudine : Double, latitudine : Double) : Int = {
        val modello=LogisticRegressionModel.load("modello")
        val prediction = modello.predict(ml.linalg.Vectors.dense(longitudine, latitudine))
        println(prediction)
        return prediction.toInt
    }






    def cluster(): String ={
         
        val data = sparkContext.textFile("../../kmeans_data.txt")
        val ds = data.map(s => s.split(' ').map(_.toDouble)).cache()
        val ds1 = ds.map(x => (x(0),x(1),x(2))).collect
        //val ds1 = sparkContext.broadCast(ds)
        var ds2 = Seq((1, ml.linalg.Vectors.dense(647.1955736966862,8716.0,45.0)))
        
        for(i <- 1 until ds1.length){
            val y = (i+1,ml.linalg.Vectors.dense(ds1(i)._1, ds1(i)._2, ds1(i)._3))
            
            ds2 = ds2 :+ y
        }
       
        val dataset = sparkSession.createDataFrame(ds2).toDF("label", "features")
       
        val kmeans = new KMeans().setK(3).setSeed(1L)
        val model = kmeans.fit(dataset)

        
        val predictions = model.transform(dataset)

       
        val evaluator = new ClusteringEvaluator()

        val silhouette = evaluator.evaluate(predictions)
        println(s"Silhouette with squared euclidean distance = $silhouette")

       
        println("Cluster Centers: ")
        model.clusterCenters.foreach(println)
        
        var result : String = " "
        model.clusterCenters.foreach(x => result = result + x + "|" )
        return result
    }





    
}
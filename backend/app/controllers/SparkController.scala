package controllers

import javax.inject._
import play.api._
import play.api.mvc._
import play.api.libs.json._
import scala.collection._
import spark._
import models._
import utile._

@Singleton
class SparkController @Inject() (
    val controllerComponents: ControllerComponents,
    val dao: Dao
) extends BaseController {

  implicit val coordinataJson = Json.format[Coordinata]
  implicit val dataOraJson = Json.format[DataOra]
  implicit val rilevazioneJson = Json.format[Rilevazione]
  implicit val SchedaTaxiJson = Json.format[SchedaTaxi]
  implicit val metricheJson = Json.format[Metriche]
  implicit val aggregatoJson = new Writes[Array[(List[Object], Int)]] {
    def writes(aggr: Array[(List[Object], Int)]): JsObject = {
      var json = Json.obj()
      for (i <- 0 until aggr.length) {
        json = json + ("" + aggr(i)._1 -> JsNumber(aggr(i)._2))
      }
      return json
    }
  }

  def testML(): Action[AnyContent] = Action {
    val risultato = dao.testML()
    Ok(Json.toJson(risultato))
  }

  def pipeline(): Action[AnyContent] = Action {
    val risultato = dao.pipeline()
    Ok(Json.toJson(risultato))
  }

  def schedaTaxi(id: Int): Action[AnyContent] = Action {
    val risultato = dao.schedaTaxi(id)
    Ok(Json.toJson(risultato))
  }

  def analisiDimensionale(dim: List[String]): Action[AnyContent] = Action {
    val risultato = dao.analisiDimensionale(dim)
    Ok(Json.toJson(risultato))
  }

  def classifica(longitudine: Double, latitudine: Double): Action[AnyContent] =
    Action {
      val ris = dao.classifica(longitudine, latitudine)
      Ok(Json.toJson(ris))
    }

  def cluster(): Action[AnyContent] = Action {
    val ris = dao.cluster()
    Ok(Json.toJson(ris))
  }

  def valori(): Action[AnyContent] = Action {
    val ris = dao.valori()
    Ok(Json.toJson("ciao"))
  }

}

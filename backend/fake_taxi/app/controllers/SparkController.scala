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
class SparkController @Inject()(val controllerComponents: ControllerComponents, val dao : Dao) extends BaseController{
    
    implicit val dataOra = Json.format[DataOra]
    implicit val taxiJson = Json.format[Taxi]
    
    def eseguiQuery : Action[AnyContent] = Action {
        val risultato=dao.query()
        Ok(risultato)
    }

    def contaTaxi : Action[AnyContent] = Action {
        val group=dao.prendiTutti()
        Ok(Json.toJson(group))
    }
}
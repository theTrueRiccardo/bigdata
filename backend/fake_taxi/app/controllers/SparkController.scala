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
    
    implicit val coordinataJson = Json.format[Coordinata]
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

    def mattia : Action[AnyContent] = Action {
        val group=dao.mattia()
        Ok(Json.toJson(group))
    }

    def orario(orarioInizio: Double ,orarioFine : Double) : Action[AnyContent] = Action {
        val group=dao.orario(orarioInizio,orarioFine)
        Ok(Json.toJson(group))
    }

    def area(inizioLong: Double ,fineLong : Double, inizioLat : Double , fineLat: Double) : Action[AnyContent] = Action {
        val group=dao.area(inizioLong,fineLong,inizioLat,fineLat)
        Ok(Json.toJson(group))
    }

    def confrontoGiornoNotte(id: Int) : Action[AnyContent] = Action {
        val group=dao.confrontoGiornoNotte(id)
        Ok(Json.toJson(group))
    }

}


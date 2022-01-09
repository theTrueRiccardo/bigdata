package controllers

import javax.inject._
import play.api._
import play.api.mvc._
import play.api.libs.json._
import scala.collection._
import spark._


@Singleton
class SparkController @Inject()(val controllerComponents: ControllerComponents, val dao : Dao) extends BaseController{
    
    def eseguiQuery : Action[AnyContent] = Action {
        val risultato=dao.query()
        Ok(risultato)
    }
}
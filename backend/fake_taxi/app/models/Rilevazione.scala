package models;
import utile._
case class Rilevazione(val id: Int, val dataOra: DataOra, val coordinata: Coordinata) extends Serializable{
    def ora() : String = {
        dataOra.ora
    }
    def data() : String ={
        dataOra.data
    }
    def longitudine() : Double ={
        coordinata.longitudine
    }
    def latitudine() : Double ={
        coordinata.latitudine
    }
}


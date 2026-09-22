package utile;
import models._
import scala.math._
import java.time.LocalDateTime
object Util extends Serializable{

    def parseRilevazione(linea : String) : Rilevazione ={
        val array=linea.split(",")
        val id=array(0).toInt
        val dataOraStringa=array(1)
        val array2=dataOraStringa.split(" ")
        val data=array2(0)
        val ora=array2(1)
        val dataOrario=LocalDateTime.parse(s"$data"+s"t$ora")
        val longitudine=array(2).toDouble
        val latitudine=array(3).toDouble
        val coordinata=new Coordinata(longitudine, latitudine)
        val rilevazione=new Rilevazione(id,dataOrario,coordinata)
        return rilevazione
    }

    def haversine(c1 : Coordinata, c2 : Coordinata) : Double = {
        var R = 6371; // Radius of the earth in km
        var dLat = deg2rad(c2.latitudine-c1.latitudine);  // deg2rad below
        var dLon = deg2rad(c2.longitudine-c1.longitudine); 
        var a = 
            sin(dLat/2) * sin(dLat/2) +
            cos(deg2rad(c1.latitudine)) * cos(deg2rad(c2.latitudine)) * 
            sin(dLon/2) * sin(dLon/2)
            ; 
        var c = 2 * atan2(sqrt(a), sqrt(1-a)); 
        var d = R * c; // Distance in km
        return d;
    }

    def deg2rad(deg : Double) : Double = {
        return deg * (Pi/180)
    }

    def minutiTra(prima : DataOra, dopo : DataOra) : Double = {
        return prima.differenzaTempoMinuti(dopo)
    }


    
}
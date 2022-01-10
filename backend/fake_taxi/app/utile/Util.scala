package utile;
import models._
class Util extends Serializable{
    def parseTaxi(linea : String) : Taxi ={
        val array=linea.split(",")
        val id=array(0).toInt
        val dataOraStringa=array(1)
        val array2=dataOraStringa.split(" ")
        val data=array2(0)
        val ora=array2(1)
        val dataOra=new DataOra(data,ora)
        val longitudine=array(2).toDouble
        val latitudine=array(3).toDouble
        val taxi=new Taxi(id,dataOra,longitudine,latitudine)
        return taxi
    }
}
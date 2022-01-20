package utile;
case class DataOra(data: String, ora: String) extends Serializable{
    val array = data.split("-")
    val anno=array(0).toInt
    val mese=array(1).toInt
    val giorno=array(2).toInt
    val array2 = ora.split(":")
    val ore=array2(0).toInt
    val minuti=array2(1).toInt
    val secondi=array2(2).toInt


    def avanzaAllaProssimaOraRestituendoMinuti() : (DataOra, Double) ={
        val minutiDaRestituire=60-minuti
        val nuovoOre=(ore+1) % 24;
        val nuovoMinuti=0
        val nuovoSecondi=0
        val nuovaOraStringa=nuovoOre+":"+nuovoMinuti+":"+nuovoSecondi;
        val dataFittizia="1-1-1"
        return (new DataOra(dataFittizia,nuovaOraStringa), minutiDaRestituire)
    }
   
}
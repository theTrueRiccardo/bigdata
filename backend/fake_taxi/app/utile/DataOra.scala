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


    def avanzaAllaProssimaOraRestituendoMinuti() : (DataOra, Double) = {
        val minutiDaRestituire=60-minuti
        val nuovoOre=(ore+1) % 24;
        val nuovoMinuti=0
        val nuovoSecondi=0
        val nuovaOra=nuovoOre+":"+nuovoMinuti+":"+nuovoSecondi;
        var nuovaData=""
        //PRE: non capita mai di dover avanzare il mese o l'anno
        if(nuovoOre==0){
            val nuovoGiorno=giorno+1;
            nuovaData = s"$anno-$mese-$nuovoGiorno" : String
        } 
        else nuovaData=data
        return (new DataOra(nuovaData,nuovaOra), minutiDaRestituire)
    }

    
    def differenzaTempoMinuti(dataOraSuccessiva : DataOra) : Double = {
        //PRE: anno e mese non cambiano mai fra le date nelle rilevazioni
        var dataOraPrecedente=new DataOra(data,ora)
        var tempo=0.0
        while(dataOraPrecedente.giorno < dataOraSuccessiva.giorno || dataOraPrecedente.ore < dataOraSuccessiva.ore){
            println(s"ciao, sto andando in looooooooooooooooooooooooooooooooop: $dataOraPrecedente $dataOraSuccessiva ")
            val avanzamento=dataOraPrecedente.avanzaAllaProssimaOraRestituendoMinuti()
            dataOraPrecedente=avanzamento._1
            tempo = tempo + avanzamento._2
        }
        tempo = tempo + (dataOraSuccessiva.minuti-dataOraPrecedente.minuti)
        return tempo
    }


   
}
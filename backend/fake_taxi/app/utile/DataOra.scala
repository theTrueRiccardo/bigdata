package utile;
case class DataOra(data: String, ora: String) extends Serializable{
    val array = data.split("-")
    private val anno=array(0).toInt
    private val mese=array(1).toInt
    private val giorno=array(2).toInt
    val array2 = ora.split(":")
    private val ore=array2(0).toInt
    private val minuti=array2(1).toInt
    private val secondi=array2(2).toInt


   
}
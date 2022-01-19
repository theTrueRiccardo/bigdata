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


   
}
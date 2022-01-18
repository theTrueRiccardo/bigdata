package models
import utile._
case class SchedaTaxi(
        val rilevazioniTotali : Int,
        val rilevazioniGiorno : Int,
        val rilevazioniNotte : Int,
        val msgConfrontoGiornoNotte : String,
        val numeroRilevazioniPerFasciaOraria : Array[(String, Int)],
        val tempoAttivo : Double,
        val tempoFermo : Double//,
        //val percorso : Array[(String, List[Coordinata])]
        ) extends Serializable
package models
import utile._
case class SchedaTaxi(
    val rilevazioniTotali: Long,
    val rilevazioniGiorno: Long,
    val rilevazioniNotte: Long,
    val msgConfrontoGiornoNotte: String,
    val numeroRilevazioniPerFasciaOraria: Array[(String, Int)],
    val tempoAttivo: Double,
    val tempoFermo: Double,
    val percorso: Array[(String, List[Coordinata])],
    val visite: Array[(String, Int)],
    val km: Double,
    val yuan: Double
) extends Serializable

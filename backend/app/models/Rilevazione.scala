package models;
import utile._
import java.time.LocalDateTime
case class Rilevazione(
    val id: Int,
    val dataOrario: LocalDateTime,
    val coordinata: Coordinata
) extends Serializable {
  def ora(): String = {
    dataOrario.toLocalTime().toString()
  }
  def data(): String = {
    dataOrario.toLocalDate().toString()
  }
  def longitudine(): Double = {
    coordinata.longitudine
  }
  def latitudine(): Double = {
    coordinata.latitudine
  }
}

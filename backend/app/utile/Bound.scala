package utile
import scala.reflect.ClassTag
import java.time.LocalTime
import java.time.temporal.ChronoUnit
class Bound(var valore : Any) extends Serializable{
    var tempo=false
    var valoreNumero=0.0
    var valoreTempo=""
    if(valore.getClass().getName().equals("java.lang.String")){
         tempo=true
         valoreTempo=valore.asInstanceOf[String] 
    }
    else {
        tempo=false
        valoreNumero=valore.asInstanceOf[Double].toDouble
    }

    def + (ampiezza : Double) : Bound = {
        if(tempo){
            val nuovoTempo=LocalTime.parse(valoreTempo)
            .plus(ampiezza.toLong, ChronoUnit.HOURS)
            var nuovoValoreTempo=""
            if(nuovoTempo.getHour()==0) nuovoValoreTempo="23:59:59"
            else nuovoValoreTempo=nuovoTempo.toString()
            new Bound(nuovoValoreTempo)
        }
        else{
            val nuovoValoreNumero=valoreNumero+ampiezza
            new Bound(nuovoValoreNumero)
        }
    }


    def < (altro : Bound) : Boolean = {
        if(tempo){
            val mio=LocalTime.parse(valoreTempo)
            val suo=LocalTime.parse(altro.valoreTempo)
            val valore = mio.compareTo(suo)
            if(valore < 0)  return true
            return false
        }
        else{
            return valoreNumero < altro.valoreNumero
        }
    }

    def >= (altro : Bound) : Boolean = {
        if(tempo){
            //println(valoreTempo)
            val mio=LocalTime.parse(valoreTempo)
            val suo=LocalTime.parse(altro.valoreTempo)
            val valore = mio.compareTo(suo)
            if(valore >= 0)  return true
            return false
        }
        else{
            return valoreNumero >= altro.valoreNumero
        }
    }

    override def toString() : String = {
        if(tempo) return valoreTempo
        return valoreNumero.toString
    }
}
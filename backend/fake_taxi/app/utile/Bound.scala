package utile
import scala.reflect.ClassTag
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
            val orario=valoreTempo.split(":")
            val ora=(orario(0)).toDouble
            val prossimaOra1=((ora+ampiezza).toInt)+""
            var prossimaOra=""
            if(prossimaOra1.length==2){
                prossimaOra=s"$prossimaOra1"
            }
            else{
                prossimaOra=s"0$prossimaOra1"
            }
            val nuovoValoreTempo=s"$prossimaOra:"+orario(1)+":"+orario(2)
            new Bound(nuovoValoreTempo)
        }
        else{
            val nuovoValoreNumero=valoreNumero+ampiezza
            new Bound(nuovoValoreNumero)
        }
    }


    def < (altro : Bound) : Boolean = {
        if(tempo){
            val orario=valoreTempo.split(":")
            val ore=orario(0).toDouble
            val minuti=orario(1).toDouble
            val secondi=orario(2).toDouble
            val orarioAltro=altro.valoreTempo.split(":")
            val oreAltro=orarioAltro(0).toDouble
            val minutiAltro=orarioAltro(1).toDouble
            val secondiAltro=orarioAltro(2).toDouble
            if(ore < oreAltro) return true
            else if(ore > oreAltro) return false
            if(minuti < minutiAltro) return true
            else if(minuti > minutiAltro) return false
            if(secondi < secondiAltro) return true
            else if(secondi > secondiAltro) return false
            return false
        }
        else{
            return valoreNumero < altro.valoreNumero
        }
    }

    def >= (altro : Bound) : Boolean = {
        if(tempo){
            val orario=valoreTempo.split(":")
            val ore=orario(0).toDouble
            val minuti=orario(1).toDouble
            val secondi=orario(2).toDouble
            val orarioAltro=altro.valoreTempo.split(":")
            val oreAltro=orarioAltro(0).toDouble
            val minutiAltro=orarioAltro(1).toDouble
            val secondiAltro=orarioAltro(2).toDouble
            if(ore > oreAltro) return true
            else if(ore < oreAltro) return false
            if(minuti > minutiAltro) return true
            else if(minuti < minutiAltro) return false
            if(secondi > secondiAltro) return true
            else if(secondi < secondiAltro) return false
            return true
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
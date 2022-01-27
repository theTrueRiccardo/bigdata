package utile
import scala.reflect.ClassTag
class Bound(var valore : Any) {
    var tempo=false
    var valoreNumero=0.0
    var valoreTempo=""
    if(ClassTag(valore.getClass) == "String"){
         tempo=true
         valoreTempo=valore.asInstanceOf[String] 
    }
    else {
        tempo=false
        valoreNumero=valore.asInstanceOf[Double]
    }

    def + (ampiezza : Double) : Bound = {
        if(tempo){
            val orario=valoreTempo.split(":")
            val ora=(orario(0)).toDouble
            val prossimaOra=ora+ampiezza
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
            val ore=orario(0).toInt
            val minuti=orario(1).toInt
            val secondi=orario(2).toInt
            val orarioAltro=altro.valoreTempo.split(":")
            val oreAltro=orarioAltro(0).toInt
            val minutiAltro=orarioAltro(1).toInt
            val secondiAltro=orarioAltro(2).toInt
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
            val ore=orario(0).toInt
            val minuti=orario(1).toInt
            val secondi=orario(2).toInt
            val orarioAltro=altro.valoreTempo.split(":")
            val oreAltro=orarioAltro(0).toInt
            val minutiAltro=orarioAltro(1).toInt
            val secondiAltro=orarioAltro(2).toInt
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
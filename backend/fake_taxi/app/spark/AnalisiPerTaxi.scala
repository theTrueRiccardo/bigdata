package spark;
import org.apache.spark.sql.SparkSession
import org.apache.spark.SparkContext
import org.apache.spark.SparkConf
import org.apache.spark.rdd.RDD
import models._
import utile._
import scala.io.Source


class AnalisiPerTaxi(sparkContext : SparkContext){

    val cartellaFileTaxi=(sys.env.get("DATASET").get.replaceAll("\\\\", "/"))
    
    def scheda(id: Int) : SchedaTaxi={
        val taxi=sparkContext.textFile(cartellaFileTaxi+id+".txt")
        val rilevazioni = taxi.map(x => Util.parseRilevazione(x))
        val confrontoGiornoNotte =  this.confrontoGiornoNotte(id,rilevazioni) 
        val rilevazioniTotali = confrontoGiornoNotte._1
        val rilevazioniGiorno = confrontoGiornoNotte._2
        val rilevazioniNotte = confrontoGiornoNotte._3
        val msgConfrontoGiornoNotte = confrontoGiornoNotte._4
        val numeroRilevazioniPerFasciaOraria = rilevazioniPerFasciaOraria(id, rilevazioni)
        val tempi = tempoOzioAttivita(id)
        var tempoAttivo=tempi._1
        var tempoFermo=tempi._2
        val percorso= this.percorso(id, rilevazioni)
        return SchedaTaxi(
            rilevazioniTotali,
            rilevazioniGiorno,
            rilevazioniNotte,
            msgConfrontoGiornoNotte,
            numeroRilevazioniPerFasciaOraria,
            tempoAttivo,
            tempoFermo,
            percorso
        )
    }

    //totali, giorno, notte, msg
    def confrontoGiornoNotte(id: Int, rilevazioni: RDD[Rilevazione]) : (Long, Long, Long, String) = {       
        val rilevazioniTotali = rilevazioni.count
        val rilevazioniGiorno = rilevazioni.filter(x => x.dataOra.ore >= 7 && x.dataOra.ore < 20).count
        val rilevazioniNotte = rilevazioniTotali - rilevazioniGiorno
        if (rilevazioniGiorno > rilevazioniNotte){
             return (
                rilevazioniTotali,
                rilevazioniGiorno,
                rilevazioniNotte,
                 s"il taxi con id: $id lavora principalmente di giorno; giorno: $rilevazioniGiorno, notte: $rilevazioniNotte."
                 )   
        }
        if (rilevazioniGiorno < rilevazioniNotte){
             return ( 
                rilevazioniTotali,
                rilevazioniGiorno,
                rilevazioniNotte,
                s"il taxi con id: $id lavora principalmente di giorno; giorno: $rilevazioniGiorno, notte: $rilevazioniNotte."
                )   
        }
        return ( 
            rilevazioniTotali,
            rilevazioniGiorno,
            rilevazioniNotte,
            s"il taxi con id: $id lavora in modo uguale sia di giorno che di notte; giorno: $rilevazioniGiorno, notte: $rilevazioniNotte."
        )    
    }

    def rilevazioniPerFasciaOraria(id : Int, rilevazioni: RDD[Rilevazione]): Array[(String, Int)] ={
        return rilevazioni.map(x => (if(x.dataOra.ore >= 0 && x.dataOra.ore < 6) "notte" else
        if(x.dataOra.ore >= 6 && x.dataOra.ore < 15) "giorno" else if(x.dataOra.ore >= 15 && x.dataOra.ore < 19)
        "pomeriggio" else "sera", 1)).reduceByKey(_+_).collect
    }


    def percorso(id : Int, rilevazioni: RDD[Rilevazione]): Array[(String, List[Coordinata])] ={
        return rilevazioni.map(x => (x.dataOra.giorno+"/"+x.dataOra.mese+"/"+x.dataOra.anno,List(x.coordinata)))
        .reduceByKey(_:::_).collect
    }

    def tempoOzioAttivita(id : Int) : (Double, Double) = {
        val filename = cartellaFileTaxi+id+".txt"
        val array=Source.fromFile.getLines.map(x => Util.parseRilevazione(x)).collect
        val tempoTotale=0.0
        val tempoAttivita=0.0
        val tempoOzio=0.0
        for(i <- 0 until array.length-1){
            val rilevazione1 = array(i)
            var j=i+1
            val rilevazione2=array(j)
            while(j < array.length && rilevazione2.coordinata == rilevazione1.coordinata){
                tempoOzio+=(differenzaTempo(rilevazione1.dataOra, rilevazione2.dataOra)
                j++;
                rilevazione2=array(j)
            }
        }
       for(i <- 1 until array.length){
           tempoTotale+=differenzaTempo(array(i-1).dataOra, array(i).dataOra)
       }
       tempoAttivita=tempoTotale-tempoOzio
       return (tempoAttivita, tempoOzio)
    }
    /*
    def listaDoppioni(id : Int) : Array[Rilevazione] ={
        //val listaCoordinate1 = coordinateDoppioni(id).foreach(println)
        val listaCoordinate = coordinateDoppioni(id)
        val taxi = logData.map(x => Util.parseTaxi(x))
        val result = taxi.filter( x => x.id == id && listaCoordinate.contains(x.coordinata)).collect
        return result
    }

    def coordinateDoppioni(id : Int) : Array[Coordinata] ={
        val taxi = logData.map(x => Util.parseRilevazione(x))
        val filtro = taxi.filter(x => x.id == id).map(x => (x.coordinata, 1)).reduceByKey(_+_).filter(_._2 > 1)
        val result = filtro.map( x => x._1).collect
        return result
    }
    */

    //mi rompeva troppo i coglioni domani lo faccio mo mi secca dio cane
    def differenzaTempo(prima : DataOra , dopo : DataOra) : Double ={
        if(prima.ore == dopo.ore) return dopo.minuti - prima.minuti
        val tempo=0.0
        do{
            tempo+=(60-prima.minuti)
            prima=new DataOra((ora)"")
        }
        val oraDopo=dopo.ore
        var oraPrima=prima.ore
        val tempo=0.0
        while(oraPrima!=oraDopo){
            tempo+=(60)
        }
        val tempo = (60 - x.minuti) + y.minuti;
        return tempo;
    } 


    
}
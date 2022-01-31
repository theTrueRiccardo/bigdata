package spark;
import org.apache.spark.sql.SparkSession
import org.apache.spark.SparkContext
import org.apache.spark.SparkConf
import org.apache.spark.rdd.RDD
import models._
import utile._
import scala.io._
import scala.util.control.Breaks._


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
        val percorso = this.percorso(id, rilevazioni)
        val visite = visiteAiLuoghi(id, rilevazioni)
        return SchedaTaxi(
            rilevazioniTotali,
            rilevazioniGiorno,
            rilevazioniNotte,
            msgConfrontoGiornoNotte,
            numeroRilevazioniPerFasciaOraria,
            tempoAttivo,
            tempoFermo,
            percorso,
            visite
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
        val file=Source.fromFile(filename)
        val array=file.getLines().toArray.map(x => Util.parseRilevazione(x))
        var tempoTotale=0.0
        var tempoAttivita=0.0
        var tempoOzio=0.0
        var i=0
        while(i < array.length-1){
            var rilevazione1 = array(i)
            var j=i+1
            var rilevazione2=array(j)
            breakable{
            while(j < array.length && rilevazione2.coordinata == rilevazione1.coordinata){
                tempoOzio=tempoOzio+(rilevazione1.dataOra.differenzaTempoMinuti(rilevazione2.dataOra))
                j+=1
                if(j == array.length) break 
                rilevazione2=array(j)
            }
            }
            i=j
            println("calcolo ozio")
        }
        println(s"lunghezza array=${array.length}")
        for(i <- 1 until array.length){
            tempoTotale=tempoTotale+(array(i-1).dataOra.differenzaTempoMinuti(array(i).dataOra))
            println(s"calcolo totale i=$i")
        }
        tempoAttivita=tempoTotale-tempoOzio
        println("sto chiudendo il file")
        file.close()
        println("sto tornando")
        return (tempoAttivita, tempoOzio)
    }

    
    def visiteAiLuoghi(id: Int, rilevazioni: RDD[Rilevazione]) : Array[(String, Int)] = {
        val scostamentoLatitudine=0.0005
        val scostamentoLongitudine=0.08
        rilevazioni.map(x => 
            if(x.latitudine >= (39.8966-scostamentoLatitudine) && x.latitudine <= (39.9081+scostamentoLatitudine) && 
            x.longitudine >= (116.3788 -scostamentoLongitudine) && x.longitudine <= (116.4042+scostamentoLongitudine) )("Piazza Tiananmen",1)
            else
            if(x.latitudine >= (39.9107-scostamentoLatitudine) && x.latitudine <= (39.9221+scostamentoLatitudine) && 
            x.longitudine >= (116.378 -scostamentoLongitudine) && x.longitudine <= (116.4035+scostamentoLongitudine) )("Città Proibita",1)
            else
            if(x.latitudine >= (39.9159-scostamentoLatitudine) && x.latitudine <= (39.937+scostamentoLatitudine) && 
            x.longitudine >= (116.3571 -scostamentoLongitudine) && x.longitudine <= (116.408+scostamentoLongitudine) )("Parco Beihai",1)
            else
            if(x.latitudine >= (39.8693-scostamentoLatitudine) && x.latitudine <= (39.8904+scostamentoLatitudine) && 
            x.longitudine >= (116.3787 -scostamentoLongitudine) && x.longitudine <= (116.4296+scostamentoLongitudine) )("Tempio del Cielo",1)
            else
            if(x.latitudine >= (39.88271-scostamentoLatitudine) && x.latitudine <= (39.88535+scostamentoLatitudine) && 
            x.longitudine >= (116.36062 -scostamentoLongitudine) && x.longitudine <= (116.36698+scostamentoLongitudine) )("Tempio Fayuan",1)
            else
            if(x.latitudine >= (31.4894-scostamentoLatitudine) && x.latitudine <= (31.5363+scostamentoLatitudine) && 
            x.longitudine >= (117.155 -scostamentoLongitudine) && x.longitudine <= (117.2568+scostamentoLongitudine) )("Hutong",1)
            else
            if(x.latitudine >= (39.36026-scostamentoLatitudine) && x.latitudine <= (39.36558+scostamentoLatitudine) && 
            x.longitudine >= (115.33327 -scostamentoLongitudine) && x.longitudine <= (115.346+scostamentoLongitudine) )("Tomba Ming",1)
            else
            if(x.latitudine >= (39.92178-scostamentoLatitudine) && x.latitudine <= (39.92705+scostamentoLatitudine) && 
            x.longitudine >= (116.38402 -scostamentoLongitudine) && x.longitudine <= (116.39675+scostamentoLongitudine) )("Parco Jingshan",1)
            else ("Altro",1)
        ).reduceByKey(_+_).collect
    }


    
}
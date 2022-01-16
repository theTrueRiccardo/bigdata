package spark;
import scala.Util._
class AnalisiDimensionale{
    //quetse dimensioni sono ordinate lessicograficamente
    val dimensioni=Array("data", "latitudine", "longitudine", "ora", "taxi")
    //l'asterisco indica un vararg, non una simpatizzazione verso la comunità LGBT
    def queryDimensionale(dimensioni : String*, rdd : RDD[Taxi])=(Object, Int){
        dimensioniOrdinate=dimensioni.sorted;
        for(numero <- 1 to 31) {
            val stringaBit = (numero).toBinaryString
            var sottoinsiemeDimensioniQuery = new ArrayBuffer[String]()
            for(indice <- 0 until stringaBit.length){
                if(stringaBit.charAt(indice)=='1'){
                    sottoinsiemeDimensioniQuery += this.dimensioni(indice)
                }
            }
            val numeroDimensioni=sottoinsiemeDimensioniQuery.length
            //if(numeroDimensioni==1){
            dim0 = sottoinsiemeDimensioniQuery(0)
            return rdd.map(x => (x.getClass.getDeclaredMethod(dim0).invoke(x) , 1)).reduceByKey(_+_).collect
            //}
            //if(numeroDimensioni==1) 
            //else if(numeroDimensioni==2)
            //else if(numeroDimensioni==3)
            //else if(numeroDimensioni==4)
            //else if(numeroDimensioni==5)
        }       
    }
}
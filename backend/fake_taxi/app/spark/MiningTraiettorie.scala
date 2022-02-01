class MiningTraiettorie(sparkContext : SparkContext){
    val fileTaxi=(sys.env.get("DATASET").get.replaceAll("\\\\", "/"))+"/dataset/dataset.txt"
    val rdd = sparkContext.textFile(fileTaxi).map(x => Util.parseRilevazione(x))

    def estraiRegole() = {
        //solo primo giorno
        val rdd1 = rdd.filter(x => x.data=="2008-02-02")
        //raggruppa in base al tempo, ogni 5 min
        var max=new Bound("23:59:59")
        var min=new Bound("00:00:00")
        var lb=new Bound(min.valore) 
        var ub=lb+ampiezza0
    }


}
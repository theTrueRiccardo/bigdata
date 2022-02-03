import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { MachineLearningService } from '../machine-learning.service';
import * as am4core from "@amcharts/amcharts4/core";
import * as am4charts from "@amcharts/amcharts4/charts";
import am4themes_animated from "@amcharts/amcharts4/themes/animated";
import * as am4maps from "@amcharts/amcharts4/maps";
import am4geodata_worldLow from "@amcharts/amcharts4-geodata/worldLow";

@Component({
  selector: 'app-machine-learning',
  templateUrl: './machine-learning.component.html',
  styleUrls: ['./machine-learning.component.css']
})
export class MachineLearningComponent implements OnInit {

   cluster: String = "Loading"
   km1 : Number = 0
   km2 : Number = 0
   km3 : Number = 0
   tAttivo1 : Number = 0
   tAttivo2 : Number = 0
   tAttivo3 : Number = 0
   tFermo1 : Number = 0
   tFermo2 : Number = 0
   tFermo3 : Number = 0
  constructor(private machineService : MachineLearningService) { }

  ngOnInit(): void {
    this.Kcluster()
    this.mappa()
  }

  Kcluster(){
    this.machineService.getCluster().subscribe(
      (response: String) => {
        this.cluster = response;
        this.tokenizza()
        this.grafico(1)
        this.grafico(2)
        this.grafico(3)

      },
      (error : HttpErrorResponse) => {
        alert(error.message);
      }
    )

  }

tokenizza(){
  let parametro: String[] = this.cluster.split("|",7)
  console.log(parametro)
  let val1 : string[] = parametro[0].split(",",7)
  let val2 : string[] = parametro[1].split(",",7)
  let val3 : string[] = parametro[2].split(",",7)
  console.log(val1)
  console.log(val2)
  console.log(val3)
  this.km1 = parseInt(val1[0].substring(2))
  this.km2 = parseInt(val2[0].substring(1))
  this.km3 = parseInt(val3[0].substring(1))
  this.tAttivo1 = parseInt(val1[1])
  this.tAttivo2 = parseInt(val2[1])
  this.tAttivo3 = parseInt(val3[1])
  this.tFermo1 = parseInt(val1[2])
  this.tFermo2 = parseInt(val2[2])
  this.tFermo3 = parseInt(val3[2])
  console.log(this.km1 , this.km2 ,this.km3, this.tAttivo1 , this.tAttivo2, this.tAttivo3 , this.tFermo1,this.tFermo2 , this.tFermo3)
}

grafico(i : Number){
  am4core.useTheme(am4themes_animated);
        let chart = am4core.create("chartdiv"+i, am4charts.XYChart);
        chart.scrollbarX = new am4core.Scrollbar();

        if ( i ==1)
            chart.data.push({
               "parametro" : "km1",
               "valore": this.km1,
            },{
              "parametro" : "km2",
              "valore": this.km2,
           },
           {
            "parametro" : "km3",
            "valore": this.km3,
         })
         if ( i ==2)
            chart.data.push({
               "parametro" : "Tempo Attivo1",
               "valore": this.tAttivo1,
            },{
              "parametro" : "Tempo Attivo2",
              "valore": this.tAttivo2,
           },
           {
            "parametro" : "Tempo Attivo3",
            "valore": this.tAttivo3,
         })
         if ( i ==3)
            chart.data.push({
               "parametro" : "Tempo Fermo1",
               "valore": this.tFermo1,
            },{
              "parametro" : "Tempo Fermo2",
              "valore": this.tFermo2,
           },
           {
            "parametro" : "Tempo Fermo3",
            "valore": this.tFermo3,
         })

    let categoryAxis = chart.xAxes.push(new am4charts.CategoryAxis());
    categoryAxis.dataFields.category = "parametro";
    categoryAxis.renderer.grid.template.location = 0;
    categoryAxis.renderer.minGridDistance = 30;
    categoryAxis.renderer.labels.template.horizontalCenter = "right";
    categoryAxis.renderer.labels.template.verticalCenter = "middle";
    categoryAxis.renderer.labels.template.rotation = 0;
    categoryAxis.tooltip!.disabled = true;
    categoryAxis.renderer.minHeight = 110;

    let valueAxis = chart.yAxes.push(new am4charts.ValueAxis());
    valueAxis.renderer.minWidth = 50;

    // Create series
    let series = chart.series.push(new am4charts.ColumnSeries());
    series.sequencedInterpolation = true;
    series.dataFields.valueY = "valore";
    series.dataFields.categoryX = "parametro";
    series.tooltipText = "{valueY}";
    series.columns.template.strokeWidth = 0;

    series.tooltip!.pointerOrientation = "vertical";

    series.columns.template.column.cornerRadiusTopLeft = 10;
    series.columns.template.column.cornerRadiusTopRight = 10;
    series.columns.template.column.fillOpacity = 0.8;

    // on hover, make corner radiuses bigger
    let hoverState = series.columns.template.column.states.create("hover");
    hoverState.properties.cornerRadiusTopLeft = 0;
    hoverState.properties.cornerRadiusTopRight = 0;
    hoverState.properties.fillOpacity = 1;

    chart.cursor = new am4charts.XYCursor();

    series.columns.template.adapter.add("fill", function(fill, target) {
      return chart.colors.getIndex(target.dataItem!.index);
    });
    // Cursor

  }

    mappa(){
      /*let map = am4core.create("mappa", am4maps.MapChart);
      //map.geodata = am4geodata_worldLow;
      console.log(map.geodata)
      map.geodataSource.url = "https://raw.githubusercontent.com/d3cn/data/master/json/geo/china/province-city/beijing.geojson";
      map.projection = new am4maps.projections.Miller(); */
      var chart2 = am4core.create("mappa", am4maps.MapChart);
      chart2.geodataSource.url = "https://raw.githubusercontent.com/MattiaCost/Pechino/main/Map.json";
      chart2.projection = new am4maps.projections.Mercator;

      chart2.homeZoomLevel = 10000;
      chart2.homeGeoPoint = {
        latitude:  39.91710957679777,
        longitude: 116.39053344726561,
      };
      var polygonSeries = chart2.series.push(new am4maps.MapPolygonSeries());

      polygonSeries.useGeodata = true;
      var polygonTemplate = polygonSeries.mapPolygons.template;

      // Configure seriesvar polygonTemplate = polygonSeries.mapPolygons.template;
      polygonTemplate.tooltipText = "{name}";
      polygonTemplate.fill = am4core.color("#8e6b3f");
      polygonTemplate.fillOpacity = 0.0;
      polygonTemplate.stroke = am4core.color("#6e5332");

      var hs = polygonTemplate.states.create("hover");
      hs.properties.fill = am4core.color("#6e5332");

      var bgSeries = chart2.series.push(new am4maps.MapImageSeries());
      bgSeries.toBack();
      var bgImage = bgSeries.mapImages.template.createChild(am4core.Image);
      bgImage.propertyFields.href = "src";
      bgImage.width = chart2.width;
      bgImage.height = chart2.height;
      bgSeries.data = [{
        src: "https://github.com/MattiaCost/Pechino/blob/main/Schermata%202022-02-03%20alle%2012.13.01.png?raw=true"
    }]

    let imageSeries = chart2.series.push(new am4maps.MapImageSeries());
    let imageSeriesTemplate = imageSeries.mapImages.template;
    let circle = imageSeriesTemplate.createChild(am4core.Circle);
    circle.radius = 7;
    circle.fill = am4core.color("red");
    circle.stroke = am4core.color("#FFFFFF");
    circle.strokeWidth = 2;
    circle.nonScaling = true;
    circle.tooltipText = "{title}";

    imageSeries.data = [{
      "latitude": 39.96141,
      "longitude": 116.39143,
    },{
      "latitude": 39.9706,
      "longitude": 116.41136,
    },{
      "latitude": 39.95595,
      "longitude": 116.41459,
    },
    {
      "latitude": 40.06875,
      "longitude": 116.56266,
    }];
    imageSeriesTemplate.propertyFields.latitude = "latitude";
    imageSeriesTemplate.propertyFields.longitude = "longitude";
 }

}



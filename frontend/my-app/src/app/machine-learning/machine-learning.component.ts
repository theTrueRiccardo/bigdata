import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { MachineLearningService } from '../machine-learning.service';
import * as am4core from "@amcharts/amcharts4/core";
import * as am4charts from "@amcharts/amcharts4/charts";
import am4themes_animated from "@amcharts/amcharts4/themes/animated";

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
  }



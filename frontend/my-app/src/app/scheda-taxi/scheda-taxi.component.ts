import { schedaTaxi } from './../schedaTaxi';
import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
import { SchedaTaxiService } from '../scheda-taxi.service';
import { HttpErrorResponse } from '@angular/common/http';
import * as am4core from '@amcharts/amcharts4/core';
import * as am4charts from '@amcharts/amcharts4/charts';
import am4themes_animated from '@amcharts/amcharts4/themes/animated';
import { coordinata } from '../coordinata';


@Component({
  selector: 'app-scheda-taxi',
  templateUrl: './scheda-taxi.component.html',
  styleUrls: ['./scheda-taxi.component.css']
})
export class SchedaTaxiComponent implements OnInit {
  id: Number = 0;
   formData: FormGroup =  new FormGroup({
    id : new FormControl,
 });
  scheda: schedaTaxi | undefined;

  constructor(private schedaService : SchedaTaxiService) { }

  ngOnInit(): void {
    this.formData = new FormGroup({
      id : new FormControl,
   });
  }

  onClickSubmit(data: any) {
    this.id = data.id;
    console.log(this.id)
    this.getScheda();
    this.giornoNotte();
    this.tempoAttivita();
    this.fascia();
    this.percorso(0);
    this.percorso(1);
    this.percorso(2);
    this.percorso(3);
    this.percorso(4);
    this.percorso(5);
    this.percorso(6);
  }

  public getScheda(): void{
    this.schedaService.getScheda(this.id).subscribe(
      (response: schedaTaxi) => {
        this.scheda = response;

      },
      (error : HttpErrorResponse) => {
        alert(error.message);
      }
    )
  }

  giornoNotte(){
    am4core.useTheme(am4themes_animated);
    let rilevazioniGiorno = this.scheda?.rilevazioniGiorno
    let rilevazioniNotte = this.scheda?.rilevazioniNotte

    let chart = am4core.create("chartdiv", am4charts.PieChart);
    chart.data = [ {
      "fascia": "Giorno",
      "rilevazioni": rilevazioniGiorno
    },{
      "fascia": "Notte",
      "rilevazioni": rilevazioniNotte
    }
    ];
    let pieSeries = chart.series.push(new am4charts.PieSeries());
    pieSeries.dataFields.value = "rilevazioni";
    pieSeries.dataFields.category = "fascia";
    pieSeries.slices.template.stroke = am4core.color("#fff");
    pieSeries.slices.template.strokeOpacity = 1;
    pieSeries.hiddenState.properties.opacity = 1;
    pieSeries.hiddenState.properties.endAngle = -90;
    pieSeries.hiddenState.properties.startAngle = -90;
    chart.hiddenState.properties.radius = am4core.percent(0);
  }

  tempoAttivita(){
    am4core.useTheme(am4themes_animated);

    let chart = am4core.create("chartdiv1", am4charts.PieChart);
    chart.hiddenState.properties.opacity = 0; // this creates initial fade-in


    chart.data = [ {
      "tempo": "Attivo",
      "rilevazioni": this.scheda?.tempoAttivo
    },{
      "tempo": "Fermo",
      "rilevazioni": this.scheda?.tempoFermo
    }
    ];
    let series = chart.series.push(new am4charts.PieSeries());
    series.dataFields.value = "rilevazioni";
    series.dataFields.radiusValue = "rilevazioni";
    series.dataFields.category = "tempo";
    series.slices.template.cornerRadius = 6;
    series.colors.step = 3;
    series.hiddenState.properties.endAngle = -90;
    chart.legend = new am4charts.Legend();
  }

  fascia(){
    am4core.useTheme(am4themes_animated);

    let chart = am4core.create("chartdiv2", am4charts.PieChart);
    chart.hiddenState.properties.opacity = 0; // this creates initial fade-in


    chart.data = [ {
      "fascia": "Notte",
      "rilevazioni": this.scheda?.numeroRilevazioniPerFasciaOraria?.[0][1]
    },{
      "fascia": "Sera",
      "rilevazioni": this.scheda?.numeroRilevazioniPerFasciaOraria?.[1][1]
    }
    ,{
      "fascia": "Giorno",
      "rilevazioni": this.scheda?.numeroRilevazioniPerFasciaOraria?.[2][1]
    }
    ,{
      "fascia": "Pomeriggio",
      "rilevazioni": this.scheda?.numeroRilevazioniPerFasciaOraria?.[3][1]
    }
    ];
    let series = chart.series.push(new am4charts.PieSeries());
    series.dataFields.value = "rilevazioni";
    series.dataFields.radiusValue = "rilevazioni";
    series.dataFields.category = "fascia";
    series.slices.template.cornerRadius = 6;
    series.colors.step = 3;
    series.hiddenState.properties.endAngle = -90;
    chart.legend = new am4charts.Legend();
  }

  percorso(giorno : any){
    am4core.useTheme(am4themes_animated);
    // Themes end
    let percorso_giorno  = this.scheda?.percorso[giorno][1]
    // Create chart instance
    let chart = am4core.create("chartdivs"+giorno, am4charts.XYChart);

    percorso_giorno?.forEach(function (value)
      {chart.data.push({
        "x": value.longitudine,
        "ay": value.latitudine,
        "aValue": 1
      })})


    // Add data


    // Create axes
    let xAxis = chart.xAxes.push(new am4charts.ValueAxis());
    xAxis.renderer.minGridDistance = 40;

    // Create value axis
    let yAxis = chart.yAxes.push(new am4charts.ValueAxis());

    // Create series
    let series1 = chart.series.push(new am4charts.LineSeries());
    series1.dataFields.valueX = "x";
    series1.dataFields.valueY = "ay";
    series1.dataFields.value = "aValue";
    series1.strokeWidth = 2;

    let bullet1 = series1.bullets.push(new am4charts.CircleBullet());
    series1.heatRules.push({
      target: bullet1.circle,
      min: 5,
      max: 20,
      property: "radius"
    });

    bullet1.tooltipText = "{valueX} x {valueY}";



    //scrollbars
    chart.scrollbarX = new am4core.Scrollbar();
    chart.scrollbarY = new am4core.Scrollbar();
  }




  }

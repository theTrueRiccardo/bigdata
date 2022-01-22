import { schedaTaxi } from './../schedaTaxi';
import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
import { SchedaTaxiService } from '../scheda-taxi.service';
import { HttpErrorResponse } from '@angular/common/http';
import * as am4core from '@amcharts/amcharts4/core';
import * as am4charts from '@amcharts/amcharts4/charts';
import am4themes_animated from '@amcharts/amcharts4/themes/animated';

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





  }

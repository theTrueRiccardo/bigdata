import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { AnanlisiDimensionaleService } from '../ananlisi-dimensionale.service';
import * as am4core from '@amcharts/amcharts4/core';
import * as am4charts from '@amcharts/amcharts4/charts';
import am4themes_animated from '@amcharts/amcharts4/themes/animated';

@Component({
  selector: 'app-analisi-dimensionale',
  templateUrl: './analisi-dimensionale.component.html',
  styleUrls: ['./analisi-dimensionale.component.css']
})
export class AnalisiDimensionaleComponent implements OnInit {

  dati : JSON | undefined;
  parametro : String = ""
  constructor(private analisiService : AnanlisiDimensionaleService) { }
  check : number = 0

  ngOnInit(): void {
    document.getElementById("loading")!.style.display = 'none';
		var list1 = document.getElementById('firstList');

		list1!.appendChild(new Option('--Select--', ''));
		list1!.appendChild(new Option('data', 'data'));
    list1!.appendChild(new Option('ora', 'ora'));
    list1!.appendChild(new Option('latitudine', 'latitudine'));
    list1!.appendChild(new Option('longitudine', 'longitudine'));
  }

   getOptions(){
    var list1 = (document.getElementById('firstList')  as HTMLSelectElement);
    var list2 = (document.getElementById("secondList") as HTMLSelectElement) ;
    var list1SelectedValue = list1?.options[list1.selectedIndex].value;

    if (list1SelectedValue=='data')
          {

      list2.options.length=0;
      list2.options[0] = new Option('Default', '-1');



    }
    else if (list1SelectedValue=='ora')
          {

      list2.options.length=0;
      list2.options[0] = new Option('12 ore', '12');
      list2.options[1] = new Option('6 ore', '6');
      list2.options[2] = new Option('4 ore', '4');


    }

    else if (list1SelectedValue=='latitudine')
          {

      list2.options.length=0;
      list2.options[0] = new Option('1', '1');
      list2.options[1] = new Option('2', '2');
      list2.options[2] = new Option('3', '3');


    }

    else if (list1SelectedValue=='longitudine')
          {
      list2.options.length=0;
      list2.options[0] = new Option('1', '1');
      list2.options[1] = new Option('0.5', '0.5');
    }
}








  onClickSubmit(parametro: String, range:String ) {
    document.getElementById("loading")!.style.display = 'block';
    this.getAnalisi(parametro,range)
   }

   showPage(){
    console.log(0)
   }

   public getAnalisi(val : String, val1 : String): void{
    this.analisiService.getAnalisi(val,val1).subscribe(
      (response: JSON ) => {
        this.dati = response;
        console.log(this.dati)
        this.grafico(response)
        this.check= 1
      },
      (error : HttpErrorResponse) => {
        alert(error.message);
      }
    )
  }

  grafico( response: JSON ){
        am4core.useTheme(am4themes_animated);
        let chart = am4core.create("chartdiv", am4charts.XYChart);
        chart.scrollbarX = new am4core.Scrollbar();

        document.getElementById("loading")!.style.display = 'none';


        let r  : String = JSON.stringify(response)
        console.log(r)

        let parametro: String[] = r.split(",",7)
        console.log(parametro)
        let param : String[] = []
        let value : Number[] = []

        for(let i = 0 ; i < parametro.length ; i++) {
          let x = parametro[i].lastIndexOf(":")
          let y = parametro[i].indexOf("(")
          let z = parametro[i].indexOf(")")
          value.push(parseInt(parametro[i].substring(x+1)))
          param.push(parametro[i].substring(y+1,z))
        }

        console.log(param);
        console.log(value)

          let k = 0
          for( k = 0 ; k < param.length  ; k ++){
            chart.data.push({
               "parametro" : param[k],
               "valore": value[k]
            })
          }



    console.log(chart.data)


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

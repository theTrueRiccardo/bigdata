import { schedaTaxi } from './../schedaTaxi';
import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
import { SchedaTaxiService } from '../scheda-taxi.service';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-scheda-taxi',
  templateUrl: './scheda-taxi.component.html',
  styleUrls: ['./scheda-taxi.component.css']
})
export class SchedaTaxiComponent implements OnInit {
  id: Number = 0;
  formData: FormGroup | undefined;
  scheda: schedaTaxi | undefined;

  constructor(private schedaService : SchedaTaxiService) { }

  ngOnInit(): void {
    this.formData = new FormGroup({
      id : new FormControl,
   });
   this.id = 1;// provvisorio
   this.getScheda();
  }

  onClickSubmit(data: any) {
    this.id = data.id;
    console.log(this.id)
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

}

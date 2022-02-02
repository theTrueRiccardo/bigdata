import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClient, HttpClientModule } from '@angular/common/http';

import { AppComponent } from './app.component';
import { SchedaTaxiComponent } from './scheda-taxi/scheda-taxi.component';
import { AppRoutingModule } from './app-routing.module';
import { HomeComponent } from './home/home.component';
import { MachineLearningComponent } from './machine-learning/machine-learning.component';
import { AnalisiDimensionaleComponent } from './analisi-dimensionale/analisi-dimensionale.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import {MatTabsModule} from '@angular/material/tabs';
import { HeatMapComponent } from './heat-map/heat-map.component';

@NgModule({
  declarations: [
    AppComponent,
    SchedaTaxiComponent,
    HomeComponent,
    MachineLearningComponent,
    AnalisiDimensionaleComponent,
    HeatMapComponent,

  ],
  imports: [
    ReactiveFormsModule,
    BrowserModule,
    HttpClientModule,
    AppRoutingModule,
    FormsModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    MatTabsModule
  ],

  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }

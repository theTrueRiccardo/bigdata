import { SchedaTaxiComponent } from './scheda-taxi/scheda-taxi.component';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './home/home.component';
import { AnalisiDimensionaleComponent } from './analisi-dimensionale/analisi-dimensionale.component';
import { MachineLearningComponent } from './machine-learning/machine-learning.component';



const routes: Routes = [
  { path: 'schedaTaxi', component: SchedaTaxiComponent },
  { path: 'analisiDimensionale', component: AnalisiDimensionaleComponent },
  { path: 'machineLearning', component: MachineLearningComponent },
  { path: 'home', component: HomeComponent },
  { path: '', redirectTo: 'home', pathMatch: 'full' }];


@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule] })
export class AppRoutingModule { }

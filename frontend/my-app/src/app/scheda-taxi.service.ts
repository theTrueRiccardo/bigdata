import { schedaTaxi } from './schedaTaxi';
import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class SchedaTaxiService {

  private apiServerUrl = environment.apiBaseUrl;

  constructor(private http : HttpClient) { }

  public getScheda(id : Number ): Observable<schedaTaxi> {
    return this.http.get<schedaTaxi>(`${this.apiServerUrl}/schedaTaxi/${id}`);
  }

}

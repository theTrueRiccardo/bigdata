import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class AnanlisiDimensionaleService {

  private apiServerUrl = environment.apiBaseUrl;

  constructor(private http : HttpClient) { }

  public getAnalisi(val : String ): Observable<JSON> {
    return this.http.get<JSON>(`${this.apiServerUrl}/analisiDimensionale?dim=${val}`);

  }

}

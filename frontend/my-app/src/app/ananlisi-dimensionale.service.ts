import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { finalize, Observable, of, pipe, share, tap } from 'rxjs';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class AnanlisiDimensionaleService {
  private cache: JSON | undefined;
  private cachedObservable?: Observable<JSON> ;
  private parametro? : String ;
  private range? : String ;
  private apiServerUrl = environment.apiBaseUrl;

  constructor(private http : HttpClient) { }

  public getAnalisi(val : String , val1 : String): Observable<JSON> {
    let observable: Observable<JSON>;
    if (this.cache && this.parametro == val && this.range == val1) {
    observable = of(this.cache);
    }  else if (this.cachedObservable && this.parametro == val && this.range == val1) {
    observable = this.cachedObservable;
    } else {
    this.cachedObservable = this.http.get<JSON>(`${this.apiServerUrl}/analisiDimensionale?dim=${val}&dim=${val1}`)
      .pipe(
        tap(res => this.cache = res),
        share(),
        finalize(() => this.cachedObservable = of() )
      );
    observable = this.cachedObservable;
    }
  return observable;
  }

}

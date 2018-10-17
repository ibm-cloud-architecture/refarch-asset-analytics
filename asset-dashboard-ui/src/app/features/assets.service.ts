
import { Injectable } from '@angular/core';
import { Asset } from './assets/asset';
import { Observable, Subject, of } from 'rxjs';
import { map, catchError } from  'rxjs/operators';
import { share} from 'rxjs/operators';
import { HttpClient } from '@angular/common/http';
// import 'rxjs/add/operator/share';
/**
Get the current top 50 assets from backend.
Subscribe to new asset events
*/
@Injectable({
  providedIn: 'root'
})
export class AssetsService {

  public assetEvents: Asset[] = [];
  private assetUrl = '/dashboardbff/assets';

  constructor(private http: HttpClient) {

  }


  public getAssets(): Observable<Asset[]> {
    if (this.assetEvents.length == 0) {
      return this.http.get<Asset[]>(this.assetUrl)
      .pipe(map(data => {
        this.assetEvents = data;
        return data;
      }))
    } else {
      return of(this.assetEvents);
    }
  }
}

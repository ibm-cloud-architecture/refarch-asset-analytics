
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
  //private assetUrl = 'http://localhost:3000/assets';
  private assetUrl = '/assets';
  private riskScoringUrl = '/asset/scoring';
  //private assetUrl = 'http://assetmgr.greencompute.ibmcase.com/assetmanager/assets'
  constructor(private http: HttpClient) {
  }


  public getAssets(): Observable<Asset[]> {
    if (this.assetEvents.length == 0) {
      return this.http.get<Asset[]>(this.assetUrl)
      .pipe(map(data => {
        this.assetEvents = data;
        for (let asset of this.assetEvents) {
            this.getRiskFactor(asset);
        }
        return this.assetEvents;
      }))
    } else {
      return of(this.assetEvents);
    }
  }

  public getRiskFactor(asset:Asset) {

    if (asset.riskRating == undefined || asset.riskRating === -1 ){
        // TODO call remote service for maintenance scoring service.
        /**
        this.http.get<Asset>(this.riskScoringUrl+"/"+asset.id);
        */
        asset.riskRating = Math.floor(Math.random() * 100) + 1;
    }
    if (asset.riskRating <= 60) {
      asset.riskColor = "#42744b";
      asset.riskRatingClass = 'Low';
    } else {
      if (asset.riskRating <= 80) {
        asset.riskColor = "#f48942";
        asset.riskRatingClass = 'Medium';
      } else {
        asset.riskColor = "#f44b42";
        asset.riskRatingClass = 'High';
      }
    }
  }
}

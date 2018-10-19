import { Component, OnInit,Input } from '@angular/core';
//Import Data points
import { AssetsService } from '../assets.service';
import { Asset } from '../assets/asset';
import { RiskCounters } from './Counter';

import { DashboardTableComponent } from './dashboard-table/dashboard-table.component';

/*
The dashboard is the main page to content map, table of assets and widgets about real time Data
*/
@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})

export class DashboardComponent implements OnInit {

  assets : Asset[];
  historyMap : Map<any, any>;
    // Data to consolidate the number of pump in each risk category
  riskCounters : RiskCounters = new RiskCounters();

  selectedAssetAnalysis : Asset;

  constructor(private service: AssetsService) {
  }

  ngOnInit() {
    //Pull Assets and Unique Assets from our Data Service
    this.service.getAssets().subscribe(
      data => {
        this.assets = data;
        this.modifyCounters();
      },
      error => { console.log(JSON.stringify(error))}
    );
    this.selectedAssetAnalysis = new Asset();
  }

  getSelectedAsset(data) {
    this.selectedAssetAnalysis = data;
  }

  modifyCounters() {
    for (let asset of this.assets) {
      if (asset.riskRatingClass === "High") {
        this.riskCounters.high += 1;
      } else if (asset.riskRatingClass === "Medium") {
        this.riskCounters.medium += 1;
      } else {
          this.riskCounters.low += 1;
      }
    }
  }

}

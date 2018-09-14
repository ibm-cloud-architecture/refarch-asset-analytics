import { Component, OnInit,Input } from '@angular/core';
//Import Data points
import { AssetsService } from '../assets.service';
import { Asset } from '../assets/asset';

import { DashboardTableComponent } from '../dashboard-table/dashboard-table.component';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})

export class DashboardComponent implements OnInit {

    assets : Asset[];
    uniqueAssets : Asset[];
    historyMap : Map<any, any>;

    riskAnalysis : any = {
        highRiskCount : 0,
        mediumRiskCount : 0,
        lowRiskCount : 0
    }

  selectedAssetAnalysis : any;

  constructor(private service: AssetsService) {
    //Pull Assets and Unique Assets from our Data Service
    this.selectedAssetAnalysis = new Asset;
    service.getAssets()
      .then((dataObject) => {
        //console.log(dataObject);
        this.assets = dataObject;
        this.riskAnalysis = {};
        this.riskAnalysis.lowRiskCount = 0;
        this.riskAnalysis.mediumRiskCount = 0;
        this.riskAnalysis.highRiskCount = 0;
        const uniqueAssetsObject = service.getUniqueAssets(this.assets);
        this.riskAnalysis = uniqueAssetsObject.riskAnalysis;
        this.uniqueAssets = uniqueAssetsObject.uniqueAssets;
        console.log("assets created in dashboard.component.ts");
      });
 }

  getSelectedAsset(data) {
    this.selectedAssetAnalysis = data;
  }

  ngOnInit() {
  }
}

import { Component, OnInit,Input } from '@angular/core';
//Import Data points
import { AssetsService } from '../assets.service';
import { Asset } from '../assets/asset';

import { DashboardTableComponent } from './dashboard-table/dashboard-table.component';

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
    this.service.getAssets().subscribe(data => {
      this.assets = data;
    });
    /*

    this.uniqueAssets = service.getUniqueAssets().uniqueAssets;
    this.riskAnalysis = {};
    this.riskAnalysis.lowRiskCount = 0;
    this.riskAnalysis.mediumRiskCount = 0;
    this.riskAnalysis.highRiskCount = 0;
    this.selectedAssetAnalysis = {};
    this.riskAnalysis = service.getUniqueAssets().riskAnalysis
    */
 }

  getSelectedAsset(data) {
    this.selectedAssetAnalysis = data;
  }

  ngOnInit() {
  }
}

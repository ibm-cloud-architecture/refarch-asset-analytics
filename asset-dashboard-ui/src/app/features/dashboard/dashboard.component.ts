import { Component, OnInit,Input } from '@angular/core';
//Import Data points
import { AssetsService } from '../assets.service';
import { Asset } from '../assets/asset';

import { DashboardTableComponent } from '../dashboard-table/dashboard-table.component';


/* 
This declare fixes the Chart error below. Since we are using the CDN,
You cannot import the Chart asset directly. So declare an anytype var
Which will allow us to skip the error. 
*/
declare var Chart: any;

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
    this.assets = service.getAssets().assets;
    this.uniqueAssets = service.getUniqueAssets().uniqueAssets;

    //console.log(this.assets);
    
    // create map with pump history
    //this.historyMap = this.initHistoryMap(this.assets);
    this.riskAnalysis = {};
    this.riskAnalysis.lowRiskCount = 0;
    this.riskAnalysis.mediumRiskCount = 0;
    this.riskAnalysis.highRiskCount = 0;
    this.selectedAssetAnalysis = {};
 }

  getSelectedAsset(data) {
    this.selectedAssetAnalysis = data;
    //console.log(this.selectedAssetAnalysis);
  }

  ngOnInit() {

    for(var i = 0; i<this.uniqueAssets.length;i++){
      if(this.uniqueAssets[i].pressure >= 100 || this.uniqueAssets[i].pressure <50){
          this.riskAnalysis.highRiskCount++;
         this.uniqueAssets[i].riskRating = 'High';
         this.uniqueAssets[i].riskColor = 'red'

       }
       else if((this.uniqueAssets[i].pressure >= 50 && this.uniqueAssets[i].pressure <60 )|| (this.uniqueAssets[i].pressure <100 && this.uniqueAssets[i].pressure >=90)){
         this.riskAnalysis.mediumRiskCount++;
         this.uniqueAssets[i].riskRating = 'Medium';
         this.uniqueAssets[i].riskColor = 'yellow';
       }
       else{
       this.riskAnalysis.lowRiskCount++;
       this.uniqueAssets[i].riskRating = 'Low';
       this.uniqueAssets[i].riskColor = 'green';
      }
   }

  
  }
  
  


}

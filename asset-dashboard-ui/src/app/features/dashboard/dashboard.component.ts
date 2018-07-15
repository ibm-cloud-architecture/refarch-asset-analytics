import { Component, OnInit,Input } from '@angular/core';
//Import Data points
import { AssetsService } from '../assets.service';
import { Asset } from '../assets/asset'
@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})




export class DashboardComponent implements OnInit {

  assets : Asset[];

  riskAnalysis : any = {
    highRiskCount : 0,
    mediumRiskCount : 0,
    lowRiskCount : 0
  }


  constructor(private service: AssetsService) {

    //Init
    this.assets = service.getAssets();
    this.riskAnalysis = {};
    this.riskAnalysis.lowRiskCount = 0;
    this.riskAnalysis.mediumRiskCount = 0;
    this.riskAnalysis.highRiskCount = 0;
    this.selectedAssetAnalysis = {};
    this.tableClick = function()

    //Risk Rating
    for(var i = 0; i<this.assets.length;i++){
       if(this.assets[i].pressure >= 100 || this.assets[i].pressure <50){
           this.riskAnalysis.highRiskCount++;
          this.assets[i].riskRating = 'High';
          this.assets[i].riskColor = 'red'
         
        }
        else if((this.assets[i].pressure >= 50 && this.assets[i].pressure <60 )|| (this.assets[i].pressure <100 && this.assets[i].pressure >=90)){
          this.riskAnalysis.mediumRiskCount++;
          this.assets[i].riskRating = 'Medium';
          this.assets[i].riskColor = 'yellow';
        }
        else{
        this.riskAnalysis.lowRiskCount++;
        this.assets[i].riskRating = 'Low';
        this.assets[i].riskColor = 'green';
       }

    }
    //Table Click
    this.tableClick = function (index){
      this.selectedAssetAnalysis = this.assets[index];
    }


console.log(this);
 }

  ngOnInit() {
  }


}


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
    //Risk Rating
    for(var i = 0; i<this.assets.length;i++){
       if(this.assets[i].pressure >= 100 || this.assets[i].pressure <50){
          this.riskAnalysis.highRiskCount++;
        }
        else if((this.assets[i].pressure >= 50 && this.assets[i].pressure <60 )|| (this.assets[i].pressure <100 && this.assets[i].pressure >=90)){
          this.riskAnalysis.mediumRiskCount++;
        }
        else{
        this.riskAnalysis.lowRiskCount++;
       }

    }
  //


console.log(this);
 }

  ngOnInit() {
  }


}

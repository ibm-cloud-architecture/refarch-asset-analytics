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
  uniqueAssets : Asset[];

  riskAnalysis : any = {
    highRiskCount : 0,
    mediumRiskCount : 0,
    lowRiskCount : 0
  }

  selectedAssetAnalysis : any;



  constructor(private service: AssetsService) {

    //Init
    this.assets = service.getAssets();
    var assets = this.assets;
    //Get Uniques
    var flags = [], output = [], l = assets.length, i, mostRecentValue;
    // set current timestamp to minimum
    mostRecentValue = 0; 
    // build map of pumps to timestamps
    var mostRecents = new Map();
    for (i = 0; i < l; i++ && assets[i]) {
      if (mostRecents.get(assets[i].id) == null) { // ID does not yet exist
        mostRecents.set(assets[i].id, i);
      }
      else { // ID exists, check timestamp against current
        if (assets[mostRecents.get(assets[i].id)].timestamp < assets[i].timestamp) {
          mostRecents.set(assets[i].id, i);
        }
      }
    }

    // output hashmap of pumps with most recent timestamps
    mostRecents.forEach(function(value, key) {
      //console.log(key + ' = ' + value);
      output.push(assets[value]);
    });

    console.log("UNIQUE ASSETS:");
    console.log(output);
    console.log("ASSETS:");
    console.log(assets);
    this.uniqueAssets = output;





    this.riskAnalysis = {};
    this.riskAnalysis.lowRiskCount = 0;
    this.riskAnalysis.mediumRiskCount = 0;
    this.riskAnalysis.highRiskCount = 0;
    this.selectedAssetAnalysis = {};
 }

 //Table Click
 tableClick(index){
  this.selectedAssetAnalysis = this.uniqueAssets[index];
}

  listAttributes(index) {
    // print attributes
    var attributes = ['id','temperature',''];
    var newAttr = document.createElement('td');
    var parent = document.getElementById("listAttributes");
    for (var x in this.uniqueAssets[index]){
      
      console.log(this.uniqueAssets[index][x]);
      newAttr.innerHTML = this.uniqueAssets[index][x];
      // append new entry
      parent.appendChild(newAttr);
    }
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

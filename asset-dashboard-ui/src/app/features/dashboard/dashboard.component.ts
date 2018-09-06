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

    // create unique assets
    this.uniqueAssets = initWithUnique(assets);

    // create map with pump history
    this.historyMap = initHistoryMap(assets);

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

// chart summary will be triggered here 
filterData(selectedAssetAnalysis) {
    console.log("selected id: "+selectedAssetAnalysis.id);
    var minDate = new Date(document.getElementById('min').value).getTime();
    var maxDate = new Date(document.getElementById('max').value).getTime();
    //console.log("date format: "+document.getElementById('min').value)
    //console.log("min date: "+minDate);
    //console.log("max date: "+maxDate);
    var pumpHistory = this.historyMap.get(selectedAssetAnalysis.id); 
    for (var i = 0; i < pumpHistory.length; i++) {
      var pumpTimeStamp = new Date(pumpHistory[i].timestamp).getTime();
      // filter values
      if (pumpTimeStamp >= minDate && pumpTimeStamp <= maxDate) {
        // plot here
        console.log("temperature: " + pumpHistory[i].temperature + "\n")
      }
    }
  }

  buildChart (){
    try( document.getElementById("myChart")){
    var ctx = document.getElementById("myChart").getContext('2d');
  console.log(ctx);
var myChart = new Chart(ctx, {
    type: 'bar',
    data: {
        labels: ["Red", "Blue", "Yellow", "Green", "Purple", "Orange"],
        datasets: [{
            label: '# of Votes',
            data: [12, 19, 3, 5, 2, 3],
            backgroundColor: [
                'rgba(255, 99, 132, 0.2)',
                'rgba(54, 162, 235, 0.2)',
                'rgba(255, 206, 86, 0.2)',
                'rgba(75, 192, 192, 0.2)',
                'rgba(153, 102, 255, 0.2)',
                'rgba(255, 159, 64, 0.2)'
            ],
            borderColor: [
                'rgba(255,99,132,1)',
                'rgba(54, 162, 235, 1)',
                'rgba(255, 206, 86, 1)',
                'rgba(75, 192, 192, 1)',
                'rgba(153, 102, 255, 1)',
                'rgba(255, 159, 64, 1)'
            ],
            borderWidth: 1
        }]
    },
    options: {
        scales: {
            yAxes: [{
                ticks: {
                    beginAtZero:true
                }
            }]
        }
    }
});
    }


  }

  ngOnInit() {

    this.buildChart();

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

  // display with latest timestamp
  function initWithUnique(assets) {

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

    return output;
  }

  // initialize map of form <pump id, list of different timestamp objects>
  function initHistoryMap(assets) {

    var historyMap = new Map();

    for (var i = 0; i < assets.length && assets[i]; i++) {
        if (historyMap.get(assets[i].id) == null) { // ID does not yet exist, add first asset to list
          var startList = []
          startList.push(assets[i]);
          historyMap.set(assets[i].id, startList);
        }
        else { // ID exists, add asset to current list
          var currentList = historyMap.get(assets[i].id);
          currentList.push(assets[i]);
          historyMap.set(assets[i].id, currentList);
        }
    }

    historyMap.forEach(function(value, key) {
      //console.log(key + ' = ' + value);
      for (var i = 0; i < value.length; i++) {
        //console.log("temperature: " + value[i].temperature + "\n")
      }
    });
    return historyMap;
  }


}

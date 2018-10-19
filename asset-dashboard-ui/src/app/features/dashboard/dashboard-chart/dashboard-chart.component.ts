import { Component, OnInit, Input } from '@angular/core';
import { Asset } from '../../assets/asset';
import { AssetsService } from '../../../features/assets.service';
import { FormControl } from '@angular/forms';

import Chart from 'chart.js';

export interface Attribute {
 value: string;
 viewValue: string;
}

/* UI in two parts, one form entry to select a time period and a type of attribute to select
 and a chart representing the evolution of the selected attribute over time
 */
@Component({
  selector: 'dashboard-chart',
  templateUrl: './dashboard-chart.component.html',
  styleUrls: ['./dashboard-chart.component.css']
})
export class DashboardChartComponent implements OnInit {
    // user has selected one asset from the table
    @Input()
    selectedAsset: Asset;

    @Input()
    dataSource: Asset[];

    historyMap : Map<any, any>;
    // Define the attributes we want to track
    attributes: Attribute[] = [
      {value: 'temperature', viewValue: 'temperature'},
      {value: 'pressure', viewValue: 'pressure'},
      {value: 'flow', viewValue: 'flow'},
      {value: 'current', viewValue: 'current'},
      {value: 'rotation', viewValue: 'rotation'}
    ];

    selectedAttribute: Attribute;

    startDate = new FormControl(new Date());
    endDate = new FormControl(new Date());

    ngOnInit() {
      //console.log(this.dataSource);
      this.historyMap = this.initHistoryMap(this.dataSource);
      var data: any = {};
      data.labels = [1500,1600,1700,1750,1800,1850,1900,1950,1999,2050];
      data.datasets = [];
      data.datasets.push(
            {
              data: [6,3,2,2,7,26,82,172,312,433],
              label: "FillerData",
              borderColor: "#c45850",
              fill: false
            });
      this.buildChart(data);
    }

    // initialize map of form <pump id, list of different timestamp objects>
    initHistoryMap(assets) {
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
      return historyMap;
    }

    // chart summary will be triggered here
    filterData(selectedAssetAnalysis) {
        // console.log("selected id: "+selectedAssetAnalysis.id);
        var minDate = this.startDate.value.getTime();
        var maxDate = this.endDate.value.getTime();
        // NaN check
        if (isNaN(minDate)) {minDate = 0;}
        if (isNaN(maxDate)) {maxDate = 9999999999999}

        // get pump history map
        var pumpHistory = this.historyMap.get(selectedAssetAnalysis.id);

        // init data
        var dataset = [];
        var label = [];

        if (this.selectedAttribute === undefined) {
          this.selectedAttribute = {value: 'temperature', viewValue: 'temperature'};
        }

        for (var i = 0; i < pumpHistory.length; i++) {
          var pumpTimeStamp = new Date(pumpHistory[i].timestamp).getTime();
          // filter values
          if (pumpTimeStamp >= minDate-86400000 && pumpTimeStamp <= maxDate) {
            // decide which attribute
            if (this.selectedAttribute.value === 'temperature') {dataset.push(pumpHistory[i].temperature);}
            else if (this.selectedAttribute.value === 'pressure') {dataset.push(pumpHistory[i].pressure);}
            else if (this.selectedAttribute.value === 'flow') {dataset.push(pumpHistory[i].flowRate);}
            else if (this.selectedAttribute.value === 'current') {dataset.push(pumpHistory[i].current);}
            else if (this.selectedAttribute.value === 'rotation') {dataset.push(pumpHistory[i].rotation);}
            label.push(pumpHistory[i].timestamp);

          }
        }
        if (label.length < 1) {
            //console.log("no data points");
            (<HTMLInputElement>document.getElementById('errorMessage')).value = "No data points in specified range!";
        }
        // add unit of measurement
        if (this.selectedAttribute.value === 'temperature') {this.selectedAttribute.value += ' (°F)';}
        else if (this.selectedAttribute.value === 'pressure') {this.selectedAttribute.value += ' (Pa)';;}
        else if (this.selectedAttribute.value === 'flow') {this.selectedAttribute.value += ' (m3/s)';}
        else if (this.selectedAttribute.value === 'current') {this.selectedAttribute.value += ' (A)';}
        else if (this.selectedAttribute.value === 'rotation') {this.selectedAttribute.value += ' (rad/s)';}
        var data: any = {};
        data.labels = label;
        data.datasets = [];
        data.datasets.push(
              {
                data: dataset,
                label: this.selectedAttribute.value,
                borderColor: "#c45850",
                fill: false
              });
        this.buildChart(data);
      }

      buildChart(data){
          console.log("build chart");
          new Chart(document.getElementById("line-chart"), {
            type: 'line',
            data: data,
            options: {
              title: {
                display: true,
                text: 'Pump Attributes Over Time'
              }
            }
          });
      }

}

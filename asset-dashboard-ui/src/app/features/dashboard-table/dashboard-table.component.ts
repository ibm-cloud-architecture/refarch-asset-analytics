import { Component, OnInit, Input, Output,EventEmitter } from '@angular/core';
import { Asset } from '../assets/asset';

import { MatTableModule } from '@angular/material';

@Component({
  selector: 'dashboard-table',
  templateUrl: './dashboard-table.component.html',
  styleUrls: ['./dashboard-table.component.css']
})
export class DashboardTableComponent implements OnInit {
    
    @Input()
    uniqueAssets: Asset[] = [];
    
    @Output()
    selectedAsset: EventEmitter<Asset> = new EventEmitter<Asset>();
    
    dataSource: Asset[];
    
    riskRatings: {};
    
    displayedColumns: string[] = ['riskColor', 'id', 'type', 'version', 'pressure', 'flowRate', 'temperature'];
    
    tableClick (i){
        //        this.selectedAsset = this.dataSource[i];
        console.log(JSON.stringify(this.dataSource[i]) + ' has been selected in the dashboard table component');
        this.selectedAsset.emit(this.dataSource[i])
    }
    
    ngOnInit() {
        this.dataSource = this.uniqueAssets;
    }

}
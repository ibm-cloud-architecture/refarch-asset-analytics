import { Component, OnInit, Input, Output,EventEmitter } from '@angular/core';
import { Asset } from '../assets/asset';

import { MatTableModule } from '@angular/material';

@Component({
  selector: 'dashboard-table',
  templateUrl: './dashboard-table.component.html',
  styleUrls: ['./dashboard-table.component.css']
})
export class DashboardTableComponent {
    
    @Input()
    dataSource: Asset[];

    @Output()
    selectedAsset: EventEmitter<Asset> = new EventEmitter<Asset>();
    
    riskRatings: {};
    
    displayedColumns: string[] = ['riskColor', 'id', 'type', 'version', 'pressure', 'flowRate', 'temperature'];
    
    tableClick (i){
        this.selectedAsset.emit(this.dataSource[i])
    }
}
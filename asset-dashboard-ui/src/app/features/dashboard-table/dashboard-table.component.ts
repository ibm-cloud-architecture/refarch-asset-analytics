import { Component, OnInit, Input, Output,EventEmitter } from '@angular/core';
import { Asset } from '../assets/asset';
import { AssetsService } from '../../features/assets.service';

import { MatTableModule } from '@angular/material';

@Component({
  selector: 'dashboard-table',
  templateUrl: './dashboard-table.component.html',
  styleUrls: ['./dashboard-table.component.css']
})
export class DashboardTableComponent implements OnInit {
    
    @Output()
    selectedAsset: EventEmitter<Asset> = new EventEmitter<Asset>();
    
    dataInput: {};
    
    dataSource: Asset[];
    
    riskRatings: {};
    
    displayedColumns: string[] = ['riskColor', 'id', 'type', 'version', 'pressure', 'flowRate', 'temperature'];

    constructor(private service: AssetsService) {
        this.dataInput = service.getUniqueAssets();
        this.dataSource = this.dataInput.uniqueAssets;
        console.log(JSON.stringify(this.dataSource));
    }

    tableClick (i){
        //        this.selectedAsset = this.dataSource[i];
        console.log(JSON.stringify(this.dataSource[i]) + ' has been selected in the dashboard table component');
        this.selectedAsset.emit(this.dataSource[i])
    }
    ngOnInit() {
    }

}

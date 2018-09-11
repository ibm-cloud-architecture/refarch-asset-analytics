import { Component, OnInit, Input } from '@angular/core';
import { Asset } from '../assets/asset';
import { AssetsService } from '../../features/assets.service';

import { MatTableModule } from '@angular/material';

@Component({
  selector: 'dashboard-table',
  templateUrl: './dashboard-table.component.html',
  styleUrls: ['./dashboard-table.component.css']
})
export class DashboardTableComponent implements OnInit {
    
    @Input()
    uAssets: Asset[];
    @Input()
    sAsset: Asset;  
    @Input()
    selectedAsset: Asset;
    
    dataSource: Asset[];
    
    displayedColumns: string[] = ['riskColor', 'id', 'type', 'version', 'pressure', 'flowRate', 'temperature'];
//    , 'type', 'version', 'pressure', 'flowRate', 'temperature'

    constructor(private service: AssetsService) { 
        this.dataSource = service.getUniqueAssets();
    }

    tableClick (i){
        this.selectedAsset = this.dataSource[i];
        console.log(JSON.stringify(this.selectedAsset) + ' has been selected in the dashboard table component');

    }
    ngOnInit() {
    }

}

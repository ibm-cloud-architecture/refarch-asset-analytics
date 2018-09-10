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
    
    dataSource: Asset[];
    
    displayedColumns: string[] = ['riskColor', 'id', 'type', 'version', 'pressure', 'flowRate', 'temperature'];
//    , 'type', 'version', 'pressure', 'flowRate', 'temperature'

//    constructor() { }
    constructor(private service: AssetsService) { 
        this.dataSource = service.getUniqueAssets();
        console.log(this.dataSource);
    }


    ngOnInit() {
        //console.log(uAssets);
    }

}

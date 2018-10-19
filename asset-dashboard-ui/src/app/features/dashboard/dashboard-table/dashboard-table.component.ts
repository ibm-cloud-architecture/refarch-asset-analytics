import { Component, OnInit, Input, Output,EventEmitter, ViewChild } from '@angular/core';
import { Asset } from '../../assets/asset';

import { MatTableModule, MatSort, MatTableDataSource } from '@angular/material';

@Component({
  selector: 'dashboard-table',
  templateUrl: './dashboard-table.component.html',
  styleUrls: ['./dashboard-table.component.css']
})
export class DashboardTableComponent {
    // present the assets in table, each asset is selectable
    @Input() data: Asset[];
    // so return the selected asset
    @Output()
    selectedAsset: EventEmitter<Asset> = new EventEmitter<Asset>();

    dataSource: MatTableDataSource<Asset>;
    riskRatings: {};

    displayedColumns: string[] = ['riskColor', 'id', 'type', 'version', 'pressure', 'flowRate', 'temperature'];

    @ViewChild(MatSort) sort: MatSort;

    ngOnInit() {
        this.dataSource = new MatTableDataSource<Asset>(this.data);
        this.dataSource.sort = this.sort;
    }

    tableClick (asset){
        this.selectedAsset.emit(asset);
    }

}

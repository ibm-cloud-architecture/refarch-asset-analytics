import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';

import { AssetsComponent } from './assets/assets.component';
import { HomeComponent } from './home/home.component';
import { SharedModule } from '../shared/shared.module';
import { DashboardComponent } from './dashboard/dashboard.component'
import { DashboardTableComponent } from './dashboard-table/dashboard-table.component';
import { DashboardChartComponent } from './dashboard-chart/dashboard-chart.component';
import { routes } from './routes';
//Angular material imports
import { MatTableModule } from '@angular/material/table';
import { MatSelectModule } from '@angular/material/select';

@NgModule({
  imports: [
    CommonModule,
    SharedModule,
    MatTableModule,
    MatSelectModule,
    RouterModule.forRoot(routes, {
    useHash: true,
    enableTracing: false
    }),
  ],
  declarations: [AssetsComponent, HomeComponent, DashboardComponent, DashboardTableComponent, DashboardChartComponent],
  exports: [ RouterModule]
})
export class FeaturesModule { }

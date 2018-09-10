import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';

import { AssetsComponent } from './assets/assets.component';
import { HomeComponent } from './home/home.component';
import { SharedModule } from '../shared/shared.module';
import { DashboardComponent } from './dashboard/dashboard.component'
import { routes } from './routes';
//Table imports
import { MatTableModule } from '@angular/material/table';
import { DashboardTableComponent } from './dashboard-table/dashboard-table.component';

@NgModule({
  imports: [
    CommonModule,
    SharedModule,
    MatTableModule,
    RouterModule.forRoot(routes, {
    useHash: true,
    enableTracing: false
    }),
  ],
  declarations: [AssetsComponent, HomeComponent, DashboardComponent, DashboardTableComponent ],
  exports: [ RouterModule]
})
export class FeaturesModule { }

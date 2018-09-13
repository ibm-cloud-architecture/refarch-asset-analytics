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
import { MatTableModule, MatSortModule} from '@angular/material';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material';
import { MatInputModule } from '@angular/material';

import { FormsModule, ReactiveFormsModule } from '@angular/forms';

@NgModule({
  imports: [
    CommonModule,
    SharedModule,
    MatTableModule,
    MatSortModule,
    MatButtonModule,
    MatSelectModule,
    MatDatepickerModule,
    MatNativeDateModule,
    MatInputModule,
    FormsModule,
    ReactiveFormsModule,
    RouterModule.forRoot(routes, {
    useHash: true,
    enableTracing: false
    }),
  ],
  declarations: [AssetsComponent, HomeComponent, DashboardComponent, DashboardTableComponent, DashboardChartComponent],
  exports: [ RouterModule]
})
export class FeaturesModule { }

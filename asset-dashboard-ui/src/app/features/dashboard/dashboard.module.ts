import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
//Angular material imports
import { MatTableModule, MatSortModule} from '@angular/material';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material';
import { MatInputModule } from '@angular/material';
// app specifics
import { DashboardComponent } from './dashboard.component'
import { DashboardTableComponent } from './dashboard-table/dashboard-table.component';
import { DashboardChartComponent } from './dashboard-chart/dashboard-chart.component';

@NgModule({
  imports: [
    CommonModule,
    MatTableModule,
    MatSortModule,
    MatButtonModule,
    MatSelectModule,
    MatDatepickerModule,
    MatNativeDateModule,
    MatInputModule,
    FormsModule,
    ReactiveFormsModule,
  ],
  declarations: [DashboardComponent, DashboardTableComponent, DashboardChartComponent]
})
export class DashboardModule { }

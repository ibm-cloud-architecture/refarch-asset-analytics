import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { routes } from './routes';



import { HomeComponent } from './home/home.component';
import { SharedModule } from '../shared/shared.module';
import { AssetsModule } from './assets/assets.module';
import { DashboardModule } from './dashboard/dashboard.module';


@NgModule({
  imports: [
    CommonModule,
    SharedModule,
    DashboardModule,
    AssetsModule,
    RouterModule.forRoot(routes, {
    useHash: true,
    enableTracing: false
    }),
  ],
  declarations: [HomeComponent],
  exports: [ RouterModule, DashboardModule, AssetsModule]
})
export class FeaturesModule { }

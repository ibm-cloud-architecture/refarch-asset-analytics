import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';

import { AssetsComponent } from './assets/assets.component';
import { HomeComponent } from './home/home.component';
import { SharedModule } from '../shared/shared.module';
import { routes } from './routes';

@NgModule({
  imports: [
    CommonModule,
    SharedModule,
    RouterModule.forRoot(routes, {
    useHash: true,
    enableTracing: false
    }),
  ],
  declarations: [AssetsComponent, HomeComponent ],
  exports: [ RouterModule]
})
export class FeaturesModule { }

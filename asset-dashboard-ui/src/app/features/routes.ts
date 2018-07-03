import { Routes } from '@angular/router';
import { HomeComponent } from './home/home.component';
import { AssetsComponent } from './assets/assets.component';
import { DashboardComponent } from './dashboard/dashboard.component'

export const routes: Routes = [
    { path: 'home', component: HomeComponent},
    { path: 'assets', component: AssetsComponent},
    { path: 'dashboard', component: DashboardComponent},
    { path: '**', redirectTo: '/home', pathMatch: 'full'}
];

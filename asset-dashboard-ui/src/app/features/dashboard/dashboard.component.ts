import { Component, OnInit,Input } from '@angular/core';
//Import Data points
import { AssetsService } from '../assets.service';
import { Asset } from '../assets/asset'
@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {

  assets : Asset[];

  constructor(private service: AssetsService) {
    this.assets = service.getAssets();
    console.log(this.assets);
 }

  ngOnInit() {
  }


}

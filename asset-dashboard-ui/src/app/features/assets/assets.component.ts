import { Component, OnInit } from '@angular/core';
import { AssetsService } from '../assets.service';
import { Asset } from './asset';

@Component({
  selector: 'app-assets',
  templateUrl: './assets.component.html',
  styleUrls: ['./assets.component.css']
})
export class AssetsComponent implements OnInit {

  constructor(private service: AssetsService ) { }
  assets: Asset[];

  ngOnInit() {
    this.assets = this.service.getAssets();
  }

}

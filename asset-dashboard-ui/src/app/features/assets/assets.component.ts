import { Component, OnInit } from '@angular/core';
import { Subscription } from 'rxjs';
import { AssetsService } from '../assets.service';
import { Asset } from './asset';

@Component({
  selector: 'app-assets',
  templateUrl: './assets.component.html',
  styleUrls: ['./assets.component.css']
})
export class AssetsComponent implements OnInit {
  public assets: Asset[];
  private socketSubscription: Subscription;

  constructor(private service: AssetsService ) {
    this.assets = this.service.getAssets().assets;
  }


  ngOnInit() {
    this.service.connect();
    this.socketSubscription = this.service.assetEvents.subscribe((message: string) => {
      console.log('received message from server: ', message)
    })
    this.service.send('1970/01/01');
  }

  ngOnDestroy() {
    this.socketSubscription.unsubscribe()
  }
}


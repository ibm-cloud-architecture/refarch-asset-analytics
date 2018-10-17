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

  /*
  Load the assets from the back end. As there are not so may pump it is fine to do so
  */
  constructor(private service: AssetsService ) {
    this.service.getAssets().subscribe(data => {
      this.assets = data;
    });
  }


  ngOnInit() {
    /**
    this.service.connect();
    this.socketSubscription = this.service.assetEvents.subscribe((message: string) => {
      console.log('received message from server: ', message)
    })
    this.service.send('1970/01/01');
    **/
  }

  ngOnDestroy() {
    // this.socketSubscription.unsubscribe()
  }
}

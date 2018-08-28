import { Component, OnInit, Input } from '@angular/core';
import { DomSanitizer, SafeStyle }  from '@angular/platform-browser';
import { AssetsService } from '../../features/assets.service';
import { Asset } from '../../features/assets/asset'
declare function require(path: string);



@Component({
  selector: 'chartSelector',
  templateUrl: './chartSelector.component.html',
  styleUrls: ['./chartSelector.component.css']
})

export class ChartSelector implements OnInit {
  @Input()
  id: string = '';
  assets : Asset[];
  @Input()
  selectedAssetID: string = '';


  constructor(private service: AssetsService) {
    this.assets = service.getAssets();
  }


  ngOnInit() {


      }

}


retrieveAsset(id){
  var returnedList = [];
  for(var i=0;i<this.assets.length;i++){
    if(id == this.assets[i].id){
      returnedList.push(this.assets[i]);

    }


  }


}

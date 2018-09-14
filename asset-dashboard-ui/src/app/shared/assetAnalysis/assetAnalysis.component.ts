import { Component, OnInit, Input } from '@angular/core';
import { DomSanitizer, SafeStyle }  from '@angular/platform-browser';
import { Asset } from '../../features/assets/asset';


@Component({
  selector: 'assetAnalysis',
  templateUrl: './assetAnalysis.component.html',
  styleUrls: ['./assetAnalysis.component.css']
})
export class AssetAnalysis{

    @Input ()
    asset: Asset;
}

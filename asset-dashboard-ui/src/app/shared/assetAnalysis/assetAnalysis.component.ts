import { Component, OnInit, Input } from '@angular/core';
import { DomSanitizer, SafeStyle }  from '@angular/platform-browser';


@Component({
  selector: 'assetAnalysis',
  templateUrl: './assetAnalysis.component.html',
  styleUrls: ['./assetAnalysis.component.css']
})
export class AssetAnalysis implements OnInit {

  
  @Input()
  id: string = 'id';
  @Input()
  type: string = 'type';
  @Input()
  version: string = 'ver';
  @Input()
  rotation: number = 0;
  @Input()
  current: number = 0;
  @Input()
  pressure: number = 0;
  @Input()
  flowRate: number = 0;
  @Input()
  temperature: number = 0;
  @Input()
  riskColor : string = 'green';
  @Input()
  longitude : string = '';
  @Input ()
  latitude : string = '';



  constructor() { }

  ngOnInit() {
      }

}

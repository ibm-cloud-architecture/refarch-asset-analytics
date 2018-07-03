import { Component, OnInit, Input } from '@angular/core';
import { DomSanitizer, SafeStyle }  from '@angular/platform-browser';

@Component({
  selector: 'assetRisk',
  templateUrl: './assetRisk.component.html',
  styleUrls: ['./assetRisk.component.css']
})
export class AssetRisk implements OnInit {
  @Input()
  id: string = '';
  @Input()
  color: string = '#8c4507';
  @Input()
  riskName: string = 'title';
  @Input()
  count: string = 'Count';


  constructor() { }

  ngOnInit() {
      }

}

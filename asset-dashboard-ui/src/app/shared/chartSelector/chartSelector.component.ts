import { Component, OnInit, Input } from '@angular/core';
import { DomSanitizer, SafeStyle }  from '@angular/platform-browser';

@Component({
  selector: 'chartSelector',
  templateUrl: './chartSelector.component.html',
  styleUrls: ['./chartSelector.component.css']
})
export class ChartSelector implements OnInit {
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

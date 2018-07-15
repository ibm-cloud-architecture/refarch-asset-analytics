import { Component, OnInit, Input } from '@angular/core';
import { DomSanitizer, SafeStyle }  from '@angular/platform-browser';

@Component({
  selector: 'leafletMap',
  templateUrl: './leafletMap.component.html',
  styleUrls: ['./leafletMap.component.css']
})
export class leafletMap implements OnInit {
  @Input()
  id: string = '';



  constructor() { }

  ngOnInit() {
      }

}

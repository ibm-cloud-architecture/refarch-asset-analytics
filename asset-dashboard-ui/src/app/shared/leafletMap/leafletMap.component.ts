import { Component, OnInit, Input } from '@angular/core';
import { DomSanitizer, SafeStyle }  from '@angular/platform-browser';

declare var L;

@Component({
  selector: 'leafletMap',
  templateUrl: './leafletMap.component.html',
  styleUrls: ['./leafletMap.component.css']
})

export class LeafletMap implements OnInit {
  @Input()
  id: string = '';


  


  constructor() { 

  }

  ngOnInit() {
    var mymap = L.map('mapid').setView([40, -100], 3.3);

    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png?{foo}', {foo: 'bar'}).addTo(mymap);

      }

  addMarker(lat,long,markerType,map){
    L.marker([lat,long]).addTo(map);
  }

}


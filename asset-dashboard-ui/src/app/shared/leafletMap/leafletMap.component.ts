import { Component, OnInit, Input, OnChanges } from '@angular/core';
import { DomSanitizer, SafeStyle }  from '@angular/platform-browser';
import { Asset } from '../../features/assets/asset';
import * as L from 'leaflet';

declare function require(path: string);

var redMapIcon = require('./redMapIcon.svg');
var greenMapIcon = require('./greenMapIcon.svg');
var yellowMapIcon = require('./yellowMapIcon.svg');

@Component({
  selector: 'leafletMap',
  templateUrl: './leafletMap.component.html',
  styleUrls: ['./leafletMap.component.css']
})

export class LeafletMap implements OnInit, OnChanges {
  @Input()
  id: string = '';
  @Input()
  assets : Asset[];

  basicIcon:L.Icon = L.icon({
    iconUrl: 'https://cdnjs.cloudflare.com/ajax/libs/leaflet/1.2.0/images/marker-icon.png'
  });
  redIcon: L.Icon = L.icon({
    iconUrl: redMapIcon,
    iconSize:     [20, 20],
    iconAnchor:   [0, 0],
    shadowAnchor: [4, 62],
    popupAnchor:  [-3, -76]
  });
  greenIcon: L.Icon = L.icon({
    iconUrl: greenMapIcon,
    iconSize:     [20, 20],
    iconAnchor:   [0, 0],
    shadowAnchor: [4, 62],
    popupAnchor:  [-3, -76]
  });
  yellowIcon: L.Icon = L.icon({
    iconUrl: yellowMapIcon,
    iconSize:     [20, 20],
    iconAnchor:   [0, 0],
    shadowAnchor: [4, 62],
    popupAnchor:  [-3, -76]
  });
  usaMap;

  ngOnInit() {
    // Render Map centered on USA, with a zoom high enough to fit the usa map
    // all mouse and touch interactions on the map are enabled, and it has zoom and attribution controls.
    this.usaMap = L.map('mapid').setView([40, -100], 4);
    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png?{foo}', {foo: 'bar',  attribution: 'OpenStreet map &copy;'
      }).addTo(this.usaMap);

  } // onInit

  addMarker(lat,long,iconType,assetID,myMap){
    console.log('Add Marker run');
    L.marker([lat,long],{icon: iconType,title:'Asset ' + assetID}).addTo(myMap);
  }

  ngOnChanges(assetUpdates) {
     console.log(JSON.stringify(assetUpdates));
     this.assets = assetUpdates.assets.currentValue;
     this.placeMarkers();
   }

   placeMarkers(){
      for(let asset of this.assets) {
          if (asset.riskRatingClass === "High") {
            this.addMarker(asset.latitude,asset.longitude,this.redIcon,asset.id,this.usaMap);
          } else if (asset.riskRatingClass === "Medium") {
            this.addMarker(asset.latitude,asset.longitude,this.yellowIcon,asset.id,this.usaMap);
          } else {
              this.addMarker(asset.latitude,asset.longitude,this.greenIcon,asset.id,this.usaMap);
          }

      }
   }
}

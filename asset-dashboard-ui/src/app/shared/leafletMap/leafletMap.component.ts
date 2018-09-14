import { Component, OnInit, Input } from '@angular/core';
import { DomSanitizer, SafeStyle }  from '@angular/platform-browser';
import { Asset } from '../../features/assets/asset'
declare function require(path: string);

var redMapIcon = require('./redMapIcon.svg');
var greenMapIcon = require('./greenMapIcon.svg');
var yellowMapIcon = require('./yellowMapIcon.svg');

declare var L;


@Component({
  selector: 'leafletMap',
  templateUrl: './leafletMap.component.html',
  styleUrls: ['./leafletMap.component.css']
})

export class LeafletMap implements OnInit {
  @Input()
  id: string = '';
  @Input()
  assets : Asset[];

  ngOnInit() {
    //Render Map
    var myMap = L.map('mapid').setView([40, -100], 3.3);
    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png?{foo}', {foo: 'bar'}).addTo(myMap);
    //Build Icon Types
    var LeafIcon = L.Icon.extend({
      options: {
          iconSize:     [20, 20],
          iconAnchor:   [0, 0],
          shadowAnchor: [4, 62],
          popupAnchor:  [-3, -76]
      }
  });

   var redIcon = new LeafIcon({iconUrl: redMapIcon});
   var greenIcon = new LeafIcon({iconUrl: greenMapIcon});
   var yellowIcon = new LeafIcon({iconUrl: yellowMapIcon});

    L.icon = function (options) {
      return new L.Icon(options);
  };
  
//Place markers
    for(var i=0;i<this.assets.length;i++)
        {
          var associatedIcon = {};
          if(this.assets[i].riskRating == 'High'){
            associatedIcon = redIcon;
          }

          if(this.assets[i].riskRating == 'Medium'){
            associatedIcon = yellowIcon;
          }

          if(this.assets[i].riskRating == 'Low'){
            associatedIcon = greenIcon;
          }

          this.addMarker(this.assets[i].latitude,this.assets[i].longitude,associatedIcon,this.assets[i].id,myMap);
        }
      }

  addMarker(lat,long,iconType,assetID,myMap){
    console.log('Add Marker run');
    L.marker([lat,long],{icon: iconType,title:'Asset ' + assetID}).addTo(myMap);
  }

  



 

 

}
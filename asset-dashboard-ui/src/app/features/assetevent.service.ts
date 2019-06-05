import { Injectable } from '@angular/core';

import * as SockJS from 'sockjs-client';
import * as Stomp from '@stomp/stompjs';
import { Observable, Subject } from 'rxjs';

import { QueueingSubject } from 'queueing-subject';


@Injectable({
  providedIn: 'root'
})
export class AsseteventService {

  public assetEvents: Observable<string>;
  private inputStream: QueueingSubject<string>
  private socket: any;
  private stompClient: any;
  private dataUrl = '/assetmanager/assets';
  assets: any;
  private getSuccess: Boolean;
  private urlDataSource: any;


  public connect() {
    if (this.assetEvents)
      return

    this.assetEvents = new Subject();
    this.socket = SockJS('http://localhost:8080/api/messages')
    this.stompClient = Stomp.over(this.socket);
    this.stompClient.connect({},
      () => {
        this.stompClient.subscribe(
          '/topic/assets',
          (message: string) => {
            // commenting out line to prevent compile error
            // this.assetEvents.next(message.body);
          }
        );
      }
    );
  }

  public send(message: string):void {
    // If the websocket is not connected then the QueueingSubject will ensure
    // that messages are queued and delivered when the websocket reconnects.
    // A regular Subject can be used to discard messages sent when the websocket
    // is disconnected.
    this.inputStream.next(message)
  }

  httpGet(theUrl) {
    var xmlHttp = new XMLHttpRequest();
    xmlHttp.open( "GET", theUrl, false ); // false for synchronous request
    xmlHttp.onreadystatechange = () => {
      if (xmlHttp.status > 202 ) { this.getSuccess = false;}
      else {
        this.getSuccess = true;
        this.urlDataSource = JSON.parse(xmlHttp.responseText);
        console.log(this.urlDataSource);
      }
      if (xmlHttp.status === 404) { console.log("** 404 Error");}
      if (xmlHttp.status === 504) { console.log("** 504 Error");}
    };
    xmlHttp.send( null );
  }


getAssets(): any {
    this.httpGet(this.dataUrl);
    console.log(this.getSuccess);
    if (this.getSuccess === true) {
      this.assets = this.urlDataSource;
    }
    else {
      this.assets = [
      {id: "12",
              os: 'raspbian',
              type: 'pump',
              version: '10',
              ipAddress: '',
              antivirus: '',
              rotation:  0,
              current:  210,
              pressure:  100,
              flowRate:  40,
               temperature:  80,
               latitude : '30.266926',
               longitude : '-97.750519',
               riskRating : 'High',
              timestamp: '2018-09-01'},

            {id: "12",
            os: 'raspbian',
            type: 'pump',
            version: '10',
            ipAddress: '',
            antivirus: '',
            rotation:  0,
            current:  210,
            pressure:  100,
            flowRate:  40,
             temperature:  120,
             latitude : '30.266926',
             longitude : '-97.750519',
             riskRating : 'High',
            timestamp: '2018-09-05'},

            {id: "100",
              os: 'raspbian',
              type: 'pump',
              version: '10.1',
               ipAddress: '',
               antivirus: '',
               rotation:  10,
               current:  220,
               pressure:  80,
               flowRate:  80,
               temperature:  60,
               latitude : '31.266926',
              longitude : '-98.750519',
              riskRating : 'Low',
              timestamp: '2'},

              {id: "100",
              os: 'raspbian',
              type: 'pump',
              version: '10.1',
               ipAddress: '',
               antivirus: '',
               rotation:  11,
               current:  227,
               pressure:  72,
               flowRate:  81,
               temperature:  62,
               latitude : '31.266926',
              longitude : '-98.750519',
              riskRating : 'Low',
              timestamp: '3'},
              {id: "104",
                os: 'raspbian',
                type: 'pump',
                version: '10.2',
                ipAddress: '',
                antivirus: '',
                rotation:  20,
                current:  220,
                pressure:  55,
                flowRate:  81,
                temperature:  60,
                latitude : '28.266926',
                longitude : '-95.750519',
                riskRating : 'Medium',
                timestamp: '2'}
              ];

    }
  }


  getUniqueAssets(): any {
    //Get Uniques
    var assets = this.assets;

    var flags = [], output = [], l = assets.length, i, mostRecentValue;
    // set current timestamp to minimum
    mostRecentValue = 0;
    // build map of pumps to timestamps
    var mostRecents = new Map();
    for (i = 0; i < l; i++ && assets[i]) {
      if (mostRecents.get(assets[i].id) == null) { // ID does not yet exist
        mostRecents.set(assets[i].id, i);
      }
      else { // ID exists, check timestamp against current
        if (assets[mostRecents.get(assets[i].id)].timestamp < assets[i].timestamp) {
          mostRecents.set(assets[i].id, i);
        }
      }
    }

    // output hashmap of pumps with most recent timestamps
    mostRecents.forEach(function(value, key) {
      //console.log(key + ' = ' + value);
      output.push(assets[value]);
    });


    var obj = {'uniqueAssets': output, 'riskAnalysis': null } //riskAnalysis}

    return obj;
  }


}

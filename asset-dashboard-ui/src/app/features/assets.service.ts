import { Injectable } from '@angular/core';
import { Asset } from './assets/asset';
import * as SockJS from 'sockjs-client';
import * as Stomp from '@stomp/stompjs';
import { Observable, Subject } from 'rxjs';
import websocketConnect from 'rxjs-websockets';
import { QueueingSubject } from 'queueing-subject';
import { share} from 'rxjs/operators';
// import 'rxjs/add/operator/share';
/**
Get the current top 50 assets from backend.
Subscribe to new asset events
*/
@Injectable({
  providedIn: 'root'
})
export class AssetsService {

  public assetEvents: Observable<string>;
  private inputStream: QueueingSubject<string>
  private socket: any;
  private stompClient: any;


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

getAssets(): any {
    var assets = [
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
             temperature:  90,
             latitude : '30.266926',
             longitude : '-97.750519',
             riskRating : 'High',
            timestamp: '2018-09-02'},

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
             temperature:  100,
             latitude : '30.266926',
             longitude : '-97.750519',
             riskRating : 'High',
            timestamp: '2018-09-03'},

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
             temperature:  110,
             latitude : '30.266926',
             longitude : '-97.750519',
             riskRating : 'High',
            timestamp: '2018-09-04'},

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
              {id: "104",
                os: 'raspbian',
                type: 'pump',
                version: '10.2',
                ipAddress: '',
                antivirus: '',
                rotation:  20,
                current:  220,
                pressure:  55,
                flowRate:  82,
                temperature:  60,
                latitude : '28.266926',
                longitude : '-95.750519',
                riskRating : 'Medium',
                timestamp: '2'}
              ];

    var riskAnalysis = this.getRiskAnalysis(assets);
    return {
            'assets': assets, 
            'riskAnalysis': riskAnalysis
            };
  }

  
  getUniqueAssets(): any {
    //Get Uniques
    var assets = this.getAssets().assets;
    
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
    var riskAnalysis = this.getRiskAnalysis(output);
      
    var obj = {'uniqueAssets': output, 'riskAnalysis': riskAnalysis}

    return obj;
  }
    
    getRiskAnalysis(assets: Asset[]): {} {
        var risks = {
            highRiskCount: 0,
            mediumRiskCount: 0,
            lowRiskCount: 0,
        };
        
        for(var i = 0; i<assets.length;i++){
            if(assets[i].pressure >= 100 || assets[i].pressure <50){
                 risks.highRiskCount++;
                 assets[i].riskRating = 'High';
                 assets[i].riskColor = 'red';
            }
            else if((assets[i].pressure >= 50 && assets[i].pressure <60 )|| (assets[i].pressure <100 && assets[i].pressure >=90)){
                 risks.mediumRiskCount++;
                 assets[i].riskRating = 'Medium';
                 assets[i].riskColor = 'yellow';
            }
            else{
                risks.lowRiskCount++;
                assets[i].riskRating = 'Low';
                assets[i].riskColor = 'green';
            }
        }
        return risks;
    }

}
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
            this.assetEvents.next(message.body);
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

  getAssets(): Asset[] {
    return [
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
              timestamp: '1'},

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
            timestamp: '2'},

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
  }
}

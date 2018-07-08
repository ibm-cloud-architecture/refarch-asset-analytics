import { Injectable } from '@angular/core';
import { Asset } from './assets/asset';
import { Observable } from 'rxjs';
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

  public connect() {
    if (this.assetEvents)
      return

    // Using share() causes a single websocket to be created when the first
    // observer subscribes. This socket is shared with subsequent observers
    // and closed when the observer count falls to zero.
    this.assetEvents = websocketConnect(
      'ws://asset.bff.green:8080/asserstream',
      this.inputStream = new QueueingSubject<string>()
    ).messages
  }

  public send(message: string):void {
    // If the websocket is not connected then the QueueingSubject will ensure
    // that messages are queued and delivered when the websocket reconnects.
    // A regular Subject can be used to discard messages sent when the websocket
    // is disconnected.
    this.inputStream.next(message)
  }

  getAssets(): Asset[] {
    return [{id: "12",
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
            timestamp: '2'},
            {id: "100",
              os: 'raspbian',
              type: 'pump',
              version: '10.1',
   	          ipAddress: '',
   	          antivirus: '',
   	          rotation:  0,
   	          current:  220,
   	          pressure:  80,
   	          flowRate:  80,
   	          temperature:  60,
              timestamp: '2'}
              ];
  }
}

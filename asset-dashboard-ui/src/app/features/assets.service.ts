import { Injectable } from '@angular/core';
import { Asset } from './assets/asset';

@Injectable({
  providedIn: 'root'
})
export class AssetsService {

  constructor() { }

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
 	          temperature:  90},
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
   	          temperature:  60}
              ];
  }

  getAssetEvent() {}
}

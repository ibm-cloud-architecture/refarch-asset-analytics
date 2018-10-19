export class Asset {
   id: string = '';
	 os?: string = '';
	 version?: string = '';
	 type?: string = '';
	 ipAddress?: string = '';
	 antivirus?: string = '';
	 rotation?: number = 0;
	 current?: number = 0;
	 pressure?: number = 0;
	 flowRate?: number = 0;
	 temperature?: number = 0;
	 riskRating?: number = -1;
   riskRatingClass?: string = '';
	 riskColor?: string = '';
	 longitude?: string = '';
	 latitude?: string = '';
   timestamp?: any;
}

import 'core-js/es6';
import 'core-js/es7/reflect';
require('zone.js/dist/zone');

import { platformBrowserDynamic } from '@angular/platform-browser-dynamic';
import { enableProdMode } from '@angular/core';

import { SampleDatePickerModule } from './sample-date-picker-module';


if (process.env.ENV === 'production') {
    enableProdMode();
}


platformBrowserDynamic().bootstrapModule(SampleDatePickerModule);
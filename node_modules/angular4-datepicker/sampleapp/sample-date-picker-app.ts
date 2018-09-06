import {Component} from '@angular/core';

declare var require: any;
const appStyles: string = require('./sample-date-picker-app.css');
const appTemplate: string = require('./sample-date-picker-app.html');

@Component({
    selector: 'mydatepicker-app',
    styles: [appStyles],
    template: appTemplate
})

export class MyDatePickerApp {

    constructor() {
        console.log('constructor: MyDatePickerApp');
    }

}

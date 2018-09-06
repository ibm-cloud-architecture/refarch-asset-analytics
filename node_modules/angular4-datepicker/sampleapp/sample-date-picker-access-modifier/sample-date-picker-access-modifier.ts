import {Component, OnInit} from '@angular/core';
import {FormGroup, FormBuilder, Validators} from '@angular/forms';
import {IMyOptions, IMyInputFieldChanged} from '../../src/my-date-picker/interfaces';

declare var require:any;
const amSampleTpl: string = require('./sample-date-picker-access-modifier.html');

@Component({
    selector: 'sample-date-picker-access-modifier',
    template: amSampleTpl
})

export class SampleDatePickerAccessModifier implements OnInit {

    private myDatePickerOptions: IMyOptions = {
        dateFormat: 'd.m.yyyy',
        height: '34px',
        width: '210px',
        inline: false
    };

    private myForm: FormGroup;

    private model: string = null;   // not initial date set (use null or empty string)
    //private model: Object = {jsdate: new Date()};   // initialize today with jsdate property
    //private model: Object = {date: {year: 2018, month: 10, day: 9}};   // this example is initialized to specific date
    //private model: Object = {formatted: '24.09.2018'};   // this example is initialized to specific date

    private selector: number = 0;

    constructor(private formBuilder: FormBuilder) { }

    ngOnInit() {
        console.log('onInit(): SampleDatePickerReactiveForms');
        this.myForm = this.formBuilder.group({
            //myDate: [null, Validators.required]   // not initial date set
            //myDate: [{jsdate: new Date()}, Validators.required] // initialize today with jsdate property
            myDate: [{date: {year: 2018, month: 10, day: 9}}, Validators.required]   // this example is initialized to specific date
        });
    }

    // ngModel functions here
    onSubmitNgModel(): void {
        console.log('Value: ', this.model);
    }

    clearNgModelDate(): void {
        this.model = null;
    }

    toggleSelector(event: any): void {
        event.stopPropagation();
        this.selector++;
    }


    // Reactive forms functions here
    onSubmitReactiveForms(): void {
        console.log('Value: ', this.myForm.controls['myDate'].value,
            ' - Valid: ', this.myForm.controls['myDate'].valid,
            ' - Invalid: ', this.myForm.controls['myDate'].invalid,
            ' - Dirty: ', this.myForm.controls['myDate'].dirty,
            ' - Pristine: ', this.myForm.controls['myDate'].pristine,
            ' - Touched: ', this.myForm.controls['myDate'].touched);
    }

    setDate(): void {
        // Set today using the setValue function
        let date: Date = new Date();
        this.myForm.patchValue({myDate: {date: {year: date.getFullYear(), month: date.getMonth() + 1, day: date.getDate()}}});
    }

    resetDate(): void {
        // Reset date picker to specific date (today)
        this.myForm.reset({myDate: {jsdate: new Date()}});
    }

    clearDate(): void {
        // Clear the date using the patchValue function (use null or empty string)
        this.myForm.patchValue({myDate: null});
    }

    onInputFieldChanged(event: IMyInputFieldChanged) {
        console.log('onInputFieldChanged(): Value: ', event.value, ' - dateFormat: ', event.dateFormat, ' - valid: ', event.valid);
    }
}

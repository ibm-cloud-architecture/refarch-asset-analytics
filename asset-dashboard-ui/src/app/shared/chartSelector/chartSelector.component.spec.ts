import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { ChartSelector } from './chartSelector.component';
import { By } from '@angular/platform-browser';

@Component({
    template: '<chartSelector id="firstTile" color="green" riskName ="Some title">'
    +'</chartSelector>'
})
class HostComponent {}

describe('chartSelector', () => {
  let component: HostComponent,
    router: Router;
  let fixture: ComponentFixture<HostComponent>;


  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [
          RouterTestingModule.withRoutes([])
      ],
      declarations: [ ChartSelector, HostComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    // fixture = TestBed.createComponent(TileComponent);
    fixture = TestBed.createComponent(HostComponent);
    router = TestBed.get(Router);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });

});

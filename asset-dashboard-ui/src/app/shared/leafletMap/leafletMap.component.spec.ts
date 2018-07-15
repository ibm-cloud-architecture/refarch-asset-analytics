import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { leafletMap } from './leafletMap.component';
import { By } from '@angular/platform-browser';

@Component({
    template: '<leafletMap id="firstTile">'
    +'</leafletMap>'
})
class HostComponent {}

describe('leafLetMapComponent', () => {
  let component: HostComponent,
    router: Router;
  let fixture: ComponentFixture<HostComponent>;


  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [
          RouterTestingModule.withRoutes([])
      ],
      declarations: [ leafletMap, HostComponent ]
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

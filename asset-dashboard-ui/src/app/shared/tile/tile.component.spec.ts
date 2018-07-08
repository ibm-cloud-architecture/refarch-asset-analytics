import { async, ComponentFixture, fakeAsync, TestBed } from '@angular/core/testing';
import {Component} from '@angular/core';
import { Router } from '@angular/router';
import { Location } from "@angular/common";
import { RouterTestingModule } from '@angular/router/testing';
import { TileComponent } from './tile.component';
import { By } from '@angular/platform-browser';

@Component({
  template: `Home`
})
export class HomeComponent {
}

@Component({
    template: '<app-tile id="firstTile" title="Some title" '
    + 'description="Some message." color="red" urlPath="home" buttonName="ask me">'
    +'</app-tile>'
})
class HostComponent {}

describe('TileComponent', () => {
  let component: HostComponent,
    router: Router;
  let mockRouter = {navigate: jasmine.createSpy('navigate')};
  let fixture: ComponentFixture<HostComponent>;


  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [
      ],
      declarations: [ TileComponent, HomeComponent, HostComponent ],
      providers: [  { provide: Router, useValue: mockRouter }]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    // mockRouter = {navigate: jasmine.createSpy('navigate')};
    fixture = TestBed.createComponent(HostComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });

  it('should contain a default title', () => {
        const element = fixture.debugElement.nativeElement.querySelector('h3');
        expect(element.textContent).toBe('Some title');
  });


  it('should contain style with red color', () => {
        const tile = fixture.debugElement.query(By.css('#firstTile'));
        const tileComponent = tile.componentInstance;
        expect(tileComponent.id).toBe('firstTile');
  });

  it('should contain style with red color', () => {
        const tile = fixture.debugElement.query(By.css('#firstTile'));
        const tileComponent = tile.componentInstance;
        expect(tileComponent.color).toBe('red');
  });

  it('should contain urlPath', () => {
        const tile = fixture.debugElement.query(By.css('#firstTile'));
        const tileComponent = tile.componentInstance;
        expect(tileComponent.urlPath).toBe('home');
  });

  it('should contain description', () => {
        const tile = fixture.debugElement.query(By.css('#firstTile'));
        const tileComponent = tile.componentInstance;
        expect(tileComponent.description).toContain('message');
  });

  it('should contain a button name', () => {
        const tile = fixture.debugElement.query(By.css('#firstTile'));
        const tileComponent = tile.componentInstance;
        expect(tileComponent.buttonName).toContain('ask');
  });

  it('should route to the url when button clicked', () => {
    const tile = fixture.debugElement.query(By.css('#firstTile'));
    expect(tile).toBeDefined();
    expect(tile.componentInstance.urlPath).toContain('home');
    tile.nativeElement.querySelector('#button').click();
    fixture.whenStable().then(() => {
        expect(mockRouter.navigate).toHaveBeenCalledWith(['home']);
    })
  });
});

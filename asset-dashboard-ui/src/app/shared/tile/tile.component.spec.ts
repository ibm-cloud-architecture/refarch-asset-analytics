import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import {Component} from '@angular/core';
import { Router } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { TileComponent } from './tile.component';
import { By } from '@angular/platform-browser';

@Component({
    template: '<app-tile id="firstTile" title="Some title" '
    + 'description="Some message." color="red" urlPath="home" buttonName="ask me">'
    +'</app-tile>'
})
class HostComponent {}

describe('TileComponent', () => {
  let component: HostComponent,
    router: Router;
  let fixture: ComponentFixture<HostComponent>;


  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [
          RouterTestingModule.withRoutes([])
      ],
      declarations: [ TileComponent, HostComponent ]
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
      const button = fixture.nativeElement.querySelector('button');
      expect(button).toBeDefined();
      button.click();
      fixture.whenStable().then(() => {
          const routerService = TestBed.get(Router);
          expect(routerService.navigate.calls.any()).toBe(true, 'navigate called');
      })
  });
});

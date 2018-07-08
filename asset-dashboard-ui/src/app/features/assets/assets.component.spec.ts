import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AssetsComponent } from './assets.component';
import { Asset } from './asset';
import { AssetsService } from '../assets.service';
import { Observable, of  } from 'rxjs';

export class AssetServiceMockup {
  public newAsset: Observable<Asset>;
  constructor() {
    let a: Asset = {id:'assetid', os : ' raspbian', version: '0.10'};
    this.newAsset = of(a);
  }
  public getAssets() {};
}

describe('AssetsComponent', () => {
  let component: AssetsComponent;
  let fixture: ComponentFixture<AssetsComponent>;
  let assetServiceMockup = {newAsset : jasmine.createSpy('newAsset'), getAssets: jasmine.createSpy('getAssets')}

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AssetsComponent ],
      providers: [ { provide: AssetsService, useClass: AssetServiceMockup}]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AssetsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

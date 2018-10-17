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
  getAssets(): Observable<Asset[]> {
    return of([{id:'asset01', os : ' raspbian', version: '0.10'},
              {id:'asset02', os : ' raspbian', version: '0.10'}]);
  }
}

describe('AssetsComponent', () => {
  let component: AssetsComponent;
  let fixture: ComponentFixture<AssetsComponent>;

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

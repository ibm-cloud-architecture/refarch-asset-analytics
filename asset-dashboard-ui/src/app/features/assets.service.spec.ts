import { TestBed, inject } from '@angular/core/testing';
import { HttpClientModule } from '@angular/common/http';
import { AssetsService } from './assets.service';

describe('AssetsService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        HttpClientModule
      ],
      providers: [AssetsService]
    });
  });

  it('should be created', inject([AssetsService], (service: AssetsService) => {
    expect(service).toBeTruthy();
  }));
});

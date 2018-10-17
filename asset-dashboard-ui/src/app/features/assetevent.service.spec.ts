import { TestBed, inject } from '@angular/core/testing';

import { AsseteventService } from './assetevent.service';

describe('AsseteventService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [AsseteventService]
    });
  });

  it('should be created', inject([AsseteventService], (service: AsseteventService) => {
    expect(service).toBeTruthy();
  }));
});

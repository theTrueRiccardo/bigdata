import { TestBed } from '@angular/core/testing';

import { AnanlisiDimensionaleService } from './ananlisi-dimensionale.service';

describe('AnanlisiDimensionaleService', () => {
  let service: AnanlisiDimensionaleService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AnanlisiDimensionaleService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});

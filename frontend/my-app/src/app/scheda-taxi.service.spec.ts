import { TestBed } from '@angular/core/testing';

import { SchedaTaxiService } from './scheda-taxi.service';

describe('SchedaTaxiService', () => {
  let service: SchedaTaxiService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SchedaTaxiService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});

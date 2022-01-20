import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SchedaTaxiComponent } from './scheda-taxi.component';

describe('SchedaTaxiComponent', () => {
  let component: SchedaTaxiComponent;
  let fixture: ComponentFixture<SchedaTaxiComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SchedaTaxiComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(SchedaTaxiComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

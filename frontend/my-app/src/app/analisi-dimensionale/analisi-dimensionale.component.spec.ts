import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AnalisiDimensionaleComponent } from './analisi-dimensionale.component';

describe('AnalisiDimensionaleComponent', () => {
  let component: AnalisiDimensionaleComponent;
  let fixture: ComponentFixture<AnalisiDimensionaleComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AnalisiDimensionaleComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AnalisiDimensionaleComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

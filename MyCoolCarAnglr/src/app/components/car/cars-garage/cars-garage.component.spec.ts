import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CarsGarageComponent } from './cars-garage.component';

describe('CarsGarageComponent', () => {
  let component: CarsGarageComponent;
  let fixture: ComponentFixture<CarsGarageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CarsGarageComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(CarsGarageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

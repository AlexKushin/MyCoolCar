import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CarCardSliderComponent } from './car-card-slider.component';

describe('CarCardSliderComponent', () => {
  let component: CarCardSliderComponent;
  let fixture: ComponentFixture<CarCardSliderComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [CarCardSliderComponent]
    });
    fixture = TestBed.createComponent(CarCardSliderComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

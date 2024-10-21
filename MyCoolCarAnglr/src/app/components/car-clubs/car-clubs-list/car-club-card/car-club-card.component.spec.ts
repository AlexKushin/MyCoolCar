import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CarClubCardComponent } from './car-club-card.component';

describe('CarClubComponent', () => {
  let component: CarClubCardComponent;
  let fixture: ComponentFixture<CarClubCardComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CarClubCardComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CarClubCardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CarClubComponent } from './car-club.component';

describe('CarClubComponent', () => {
  let component: CarClubComponent;
  let fixture: ComponentFixture<CarClubComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CarClubComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(CarClubComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

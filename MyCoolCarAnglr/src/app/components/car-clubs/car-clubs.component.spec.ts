import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CarClubsComponent } from './car-clubs.component';

describe('CarClubsComponent', () => {
  let component: CarClubsComponent;
  let fixture: ComponentFixture<CarClubsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CarClubsComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(CarClubsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CarClubAdminPageComponent } from './car-club-admin-page.component';

describe('CarClubAdminPageComponent', () => {
  let component: CarClubAdminPageComponent;
  let fixture: ComponentFixture<CarClubAdminPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CarClubAdminPageComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(CarClubAdminPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

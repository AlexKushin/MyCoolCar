import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NewCarClubComponent } from './new-car-club.component';

describe('NewCarClubComponent', () => {
  let component: NewCarClubComponent;
  let fixture: ComponentFixture<NewCarClubComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [NewCarClubComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(NewCarClubComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

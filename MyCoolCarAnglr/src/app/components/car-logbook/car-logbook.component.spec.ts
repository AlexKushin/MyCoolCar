import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CarLogbookComponent } from './car-logbook.component';

describe('CarLogbookComponent', () => {
  let component: CarLogbookComponent;
  let fixture: ComponentFixture<CarLogbookComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CarLogbookComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(CarLogbookComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

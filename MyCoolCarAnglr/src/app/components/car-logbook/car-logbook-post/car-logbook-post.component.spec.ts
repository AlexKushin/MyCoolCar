import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CarLogbookPostComponent } from './car-logbook-post.component';

describe('CarLogbookPostComponent', () => {
  let component: CarLogbookPostComponent;
  let fixture: ComponentFixture<CarLogbookPostComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CarLogbookPostComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(CarLogbookPostComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

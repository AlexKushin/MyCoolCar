import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NewCarComponent } from './new-car.component';

describe('CarComponent', () => {
  let component: NewCarComponent;
  let fixture: ComponentFixture<NewCarComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [NewCarComponent]
    });
    fixture = TestBed.createComponent(NewCarComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

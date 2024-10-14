import {ComponentFixture, TestBed} from '@angular/core/testing';

import {CarClubsListComponent} from './car-clubs-list.component';

describe('CarClubsListComponent', () => {
  let component: CarClubsListComponent;
  let fixture: ComponentFixture<CarClubsListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CarClubsListComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(CarClubsListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

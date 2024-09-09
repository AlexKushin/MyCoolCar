import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NewCarLogbookPostComponent } from './new-car-logbook-post.component';

describe('NewCarLogbookPostComponent', () => {
  let component: NewCarLogbookPostComponent;
  let fixture: ComponentFixture<NewCarLogbookPostComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [NewCarLogbookPostComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(NewCarLogbookPostComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

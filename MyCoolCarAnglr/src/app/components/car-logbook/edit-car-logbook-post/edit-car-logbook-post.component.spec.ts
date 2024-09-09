import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EditCarLogbookPostComponent } from './edit-car-logbook-post.component';

describe('EditCarLogbookPostComponent', () => {
  let component: EditCarLogbookPostComponent;
  let fixture: ComponentFixture<EditCarLogbookPostComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EditCarLogbookPostComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(EditCarLogbookPostComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

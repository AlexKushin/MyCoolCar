import {Component, OnInit} from '@angular/core';
import {FormControl, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {Store} from "@ngrx/store";
import {ActivatedRoute} from "@angular/router";
import * as CarLogbookActions from '../store/car-logbook.actions'
import * as fromApp from '../../../store/app.reducer';


@Component({
  selector: 'app-new-car-logbook-post',
  standalone: true,
  imports: [
    ReactiveFormsModule
  ],
  templateUrl: './new-car-logbook-post.component.html',
  styleUrl: './new-car-logbook-post.component.css'
})
export class NewCarLogbookPostComponent implements OnInit {

  constructor(
    private store: Store<fromApp.AppState>,
    private route: ActivatedRoute
  ) {
  }

  carLogbookPostForm: FormGroup;

  ngOnInit(): void {
    this.initForm()
  }


  initForm() {
    let carLogbookPostTopic = '';
    let carLogbookPostDescription = '';


    this.carLogbookPostForm = new FormGroup({
      'topic': new FormControl(carLogbookPostTopic, Validators.required),
      'description': new FormControl(carLogbookPostDescription, Validators.required),
      // 'mainImage': new FormControl(this.mainImage, Validators.required),
      // 'file': new FormControl(this.images, Validators.required)
    });
  }

  saveCarLogbookPost() {
    const snapshot = this.route.snapshot;
    const carLogbookId = +snapshot.paramMap.get('car-logbookId');

    this.store.dispatch(new CarLogbookActions.AddCarLogbookPost(
        {
          carLogbookId: carLogbookId,
          logbookPost: this.carLogbookPostForm.value
        }
      )
    )
  }
}

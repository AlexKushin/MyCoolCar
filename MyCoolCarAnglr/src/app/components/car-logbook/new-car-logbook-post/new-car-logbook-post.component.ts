import {Component, OnInit} from '@angular/core';
import {FormControl, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {Store} from "@ngrx/store";

import * as fromCarLogbook from "../store/car-logbook.reducer";
import * as CarLogbookActions from '../store/car-logbook.actions'
import {ActivatedRoute} from "@angular/router";


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
    private store: Store<{ carLogbookState: fromCarLogbook.State }>,
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
    console.log("Car Logbook Post ")
    console.log(this.carLogbookPostForm.value)

    const snapshot = this.route.snapshot;
    const carLogbookId = +snapshot.paramMap.get('car-logbookId');


    console.log("carLogbookId")
    console.log(carLogbookId)

    this.store.dispatch(new CarLogbookActions.AddCarLogbookPost(
        {
          carLogbookId: carLogbookId,
          logbookPost: this.carLogbookPostForm.value
        }
      )
    )
  }
}

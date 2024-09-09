import {Component, OnDestroy, OnInit} from '@angular/core';
import {FormControl, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {ActivatedRoute} from "@angular/router";
import {Store} from "@ngrx/store";
import * as fromCarLogbook from "../store/car-logbook.reducer";
import * as CarLogbookActions from '../store/car-logbook.actions'


import {CarLogbookPost} from "../../../models/carLogbookPost";


@Component({
  selector: 'app-edit-car-logbook-post',
  standalone: true,
  imports: [
    ReactiveFormsModule
  ],
  templateUrl: './edit-car-logbook-post.component.html',
  styleUrl: './edit-car-logbook-post.component.css'
})
export class EditCarLogbookPostComponent implements OnInit, OnDestroy {

  carLogbookPost: CarLogbookPost;
  mainImage: any;
  images: any;
  carLogbookPostForm: FormGroup;

  constructor(
    private route: ActivatedRoute,
    private store: Store<{ carLogbookState: fromCarLogbook.State }>
  ) {
  }


  ngOnInit(): void {
    this.carLogbookPost = history.state.data;
    this.initForm()
  }

  ngOnDestroy(): void {
  }

  saveEditedCarLogbookPost() {
    const snapshot = this.route.snapshot;
    const carLogbookId = +snapshot.paramMap.get('car-logbookId');
    this.store.dispatch(new CarLogbookActions.EditCarLogbookPost({
      carLogbookId: carLogbookId,
      logbookPost: this.carLogbookPostForm.value
    }))

  }

  initForm() {
    this.carLogbookPostForm = new FormGroup({
      'topic': new FormControl(this.carLogbookPost.topic, Validators.required),
      'description': new FormControl(this.carLogbookPost.description, Validators.required),
      // 'mainImage': new FormControl(this.mainImage, Validators.required),
      // 'file': new FormControl(this.images, Validators.required)
    });
  }
}

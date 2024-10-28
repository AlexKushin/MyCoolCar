import {Component, OnInit} from '@angular/core';
import {FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators} from "@angular/forms";
import {Store} from "@ngrx/store";
import * as fromApp from "../../../store/app.reducer";
import * as CarClubsActions from "../store/car-club.actions"
import {ActivatedRoute} from "@angular/router";

@Component({
  selector: 'app-new-car-club',
  standalone: true,
  imports: [
    FormsModule,
    ReactiveFormsModule
  ],
  templateUrl: './new-car-club.component.html',
  styleUrl: './new-car-club.component.css'
})
export class NewCarClubComponent implements OnInit {

  constructor(
    private store: Store<fromApp.AppState>,
    private route: ActivatedRoute
  ) {
  }

  carClubCreatingForm: FormGroup;
  mainImage: any;

  ngOnInit(): void {
    this.initForm()
  }


  initForm() {
    let carClubName = '';
    let carClubDescription = '';
    let carClubAccessType = '';
    let carClubLocation = '';


    this.carClubCreatingForm = new FormGroup({
      'name': new FormControl(carClubName, Validators.required),
      'description': new FormControl(carClubDescription, Validators.required),
      'accessType': new FormControl(carClubAccessType, Validators.required),
      'location': new FormControl(carClubLocation, Validators.required),
      'mainImage': new FormControl(this.mainImage, Validators.required),
      // 'file': new FormControl(this.images, Validators.required)
    });
  }

  createCarClub() {

    let formData: any = new FormData();
    Object.keys(this.carClubCreatingForm.controls).forEach((formControlName: string) => {

      if (formControlName === 'mainImage') {
        formData.append('mainImage', this.mainImage)
      } else {
        const formControl = this.carClubCreatingForm.get(formControlName);
        if (formControl) {
          formData.append(formControlName, formControl.value);
        }
      }
    });
    this.store.dispatch(new CarClubsActions.CreateCarClub(formData))
  }

  uploadMainImage(event: any) {
    this.mainImage = event.target.files[0];
  }
}

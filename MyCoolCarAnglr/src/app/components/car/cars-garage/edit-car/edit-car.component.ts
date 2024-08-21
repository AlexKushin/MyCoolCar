import {Component, OnInit} from '@angular/core';
import {FormControl, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {Store} from "@ngrx/store";
import * as fromUserCars from "../../store/cars.reducer";
import * as UserCarsActions from '../../store/cars.actions'
import {Car} from "../../../../models/car";
import {NgForOf} from "@angular/common";

@Component({
  selector: 'app-edit-car',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    NgForOf
  ],
  templateUrl: './edit-car.component.html',
  styleUrl: './edit-car.component.css'
})
export class EditCarComponent implements OnInit {

  constructor(
    private store: Store<{ userCarsState: fromUserCars.State }>
  ) {
  }
  car: Car;
  mainImage: any;
  images: any;
  carForm: FormGroup;

  ngOnInit() {
    this.car = history.state.data;

    console.log(this.car);
    this.initForm();
  }

  saveCar() {
    let formData: any = new FormData();
    Object.keys(this.carForm.controls).forEach((formControlName: string) => {
     /* if (formControlName === 'file') {
        for (const image of this.images) {
          formData.append('files[]', image);
        }
      }*/
      /*if (formControlName === 'mainImage') {
        formData.append('mainImage', this.mainImage)
      } else {*/
      console.log("form control name= " + this.carForm.get(formControlName))
        const formControl = this.carForm.get(formControlName);
        if (formControl) {
          console.log("formControlName= "+formControlName)
          console.log("formControl.value= "+ formControl.value)
          formData.append(formControlName, formControl.value);
        }
     // }
    });
    console.log("EDIT CAR COMPONENT formdata")
    console.log(formData)
    this.store.dispatch(new UserCarsActions.UpdateUserCar({ id: this.car.id, userCar: formData }))
  }

  uploadMainImage(event: any) {
    this.mainImage = event.target.files[0];
  }

  uploadImages(event: any) {
    this.images = event.target.files;
  }

  initForm() {
    let carBrand = '';
    let carModel = '';
    let carProductYear = 1900;
    let carDescription = '';


    this.carForm = new FormGroup({
      'brand': new FormControl(this.car.brand, Validators.required),
      'model': new FormControl(this.car.model, Validators.required),
      'productYear': new FormControl(this.car.productYear, Validators.required),
      'description': new FormControl(this.car.description, Validators.required),
      //'mainImage': new FormControl(this.car.mainImageUrl, Validators.required),
      //'file': new FormControl(this.car.imagesUrl, Validators.required)
    });
  }

}

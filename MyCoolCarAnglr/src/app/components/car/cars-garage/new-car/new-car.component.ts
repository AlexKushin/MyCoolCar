import {Component, OnInit} from '@angular/core';
import {CommonModule} from '@angular/common';
import {FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators} from "@angular/forms";
import {Store} from "@ngrx/store";
import * as fromUserCars from "../../store/cars.reducer";
import * as UserCarsActions from '../../store/cars.actions'

@Component({
  selector: 'new-car',
  standalone: true,
  imports: [CommonModule, FormsModule, ReactiveFormsModule],
  templateUrl: './new-car.component.html',
  styleUrls: ['./new-car.component.css']
})
export class NewCarComponent implements OnInit {

  constructor(
    private store: Store<{ userCarsState: fromUserCars.State }>
  ) {
  }

  mainImage: any;
  images: any;
  carForm: FormGroup;

  ngOnInit() {
    this.initForm();
  }

  saveCar() {
    let formData: any = new FormData();
    Object.keys(this.carForm.controls).forEach((formControlName: string) => {
      if (formControlName === 'file') {
        for (const image of this.images) {
          formData.append('files[]', image);
        }
      }
      if (formControlName === 'mainImage') {
        formData.append('mainImage', this.mainImage)
      } else {
        const formControl = this.carForm.get(formControlName);
        if (formControl) {
          formData.append(formControlName, formControl.value);
        }
      }
    });
    console.log("NEW CAR COMPONENT formData")
    console.log(formData)
    this.store.dispatch(new UserCarsActions.AddUserCar(formData))
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
      'brand': new FormControl(carBrand, Validators.required),
      'model': new FormControl(carModel, Validators.required),
      'productYear': new FormControl(carProductYear, Validators.required),
      'description': new FormControl(carDescription, Validators.required),
      'mainImage': new FormControl(this.mainImage, Validators.required),
      'file': new FormControl(this.images, Validators.required)
    });
  }

}

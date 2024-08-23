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
    this.store.dispatch(new UserCarsActions.UpdateUserCar({id: this.car.id, userCar: this.carForm.value}))
  }

  uploadMainImage(event: any) {
    this.mainImage = event.target.files[0];
  }

  uploadImages(event: any) {
    this.images = event.target.files;
  }

  initForm() {
    this.carForm = new FormGroup({
      'brand': new FormControl(this.car.brand, Validators.required),
      'model': new FormControl(this.car.model, Validators.required),
      'productYear': new FormControl(this.car.productYear, Validators.required),
      'description': new FormControl(this.car.description, Validators.required),
    });
  }

}

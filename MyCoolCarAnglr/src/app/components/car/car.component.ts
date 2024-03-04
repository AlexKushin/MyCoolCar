import {Component, OnInit} from '@angular/core';
import {CommonModule} from '@angular/common';
import {FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators} from "@angular/forms";
import {CarService} from "../../services/car.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-car',
  standalone: true,
  imports: [CommonModule, FormsModule, ReactiveFormsModule],
  templateUrl: './car.component.html',
  styleUrls: ['./car.component.css']
})
export class CarComponent implements OnInit {

  constructor(
    private carService: CarService,
    private router: Router,
  ) {
  }
  mainImage: any;
  images: any;
  // @ts-ignore
  carForm: FormGroup;

  ngOnInit() {
    this.initForm();
  }

  saveCar() {
    console.log("save car")


    let formData: any = new FormData();
    Object.keys(this.carForm.controls).forEach(formControlName => {
      if (formControlName === 'file') {
        for (let i = 0; i < this.images.length; i++) {
         formData.append('files[]', this.images[i])
        }
      }
      if (formControlName === 'mainImage'){
        formData.append('mainImage', this.mainImage)
      }
      else {
        // @ts-ignore
        formData.append(formControlName, this.carForm.get(formControlName).value);
      }
    });
    console.log(formData)
    this.carService.addNewCar(formData).subscribe(
      data => {
        console.log(data)
        this.router.navigate(["welcome"])
      },
      error => {
        console.log(error)
      }
    )
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

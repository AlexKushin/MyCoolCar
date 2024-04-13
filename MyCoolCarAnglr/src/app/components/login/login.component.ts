import {Component, OnInit} from '@angular/core';
import {CommonModule} from '@angular/common';
import {Router, RouterLink} from "@angular/router";
import {FormsModule, NgForm} from '@angular/forms';
import {AuthenticationService} from "../../services/authServices/authentication.service";
import {Car} from "../../models/car";
import {CarService} from "../../services/car.service";
import {CarCardComponent} from "../car/car-card/car-card.component";
import {CarCardSliderComponent} from "../car/car-card-slider/car-card-slider.component";
import * as fromAuth from './store/auth.reducer'
import {Store} from "@ngrx/store";
import * as AuthActions from './store/auth.actions'

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule, CarCardComponent, CarCardSliderComponent, RouterLink],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {


  username = ""
  password = ""
  errorMessage = "Invalid Credentials"
  invalidLogin = false
  cars: Car[] = []

  constructor(
    //private router: Router,
    private carService: CarService,
   // private authService: AuthenticationService,
    private store: Store<fromAuth.State>
  ) {
  }

  ngOnInit(): void {
   // this.refreshTopCars()
  }


  refreshTopCars() {
    this.carService.getTopCars().subscribe(
      response => {
        console.log(response)
        this.cars = response;
      }
    )
  }

  onSubmit(authForm: NgForm) {
    if (!authForm.valid) {
      return;
    }
    const email = authForm.value.email;
    const password = authForm.value.password;

    this.store.dispatch(new AuthActions.LoginStart(
      {email: email, password: password}))
    authForm.reset();
  }
}

import {Component, OnInit} from '@angular/core';
import {CommonModule} from '@angular/common';
import {Router} from "@angular/router";
import {FormsModule} from '@angular/forms';
import {AuthenticationService} from "../../services/authServices/authentication.service";
import {Car} from "../../models/car";
import {CarService} from "../../services/car.service";
import {CarCardComponent} from "../car-card/car-card.component";
import {CarCardSliderComponent} from "../car-card-slider/car-card-slider.component";

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule, CarCardComponent, CarCardSliderComponent],
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
    private router: Router,
    private carService: CarService,
    private authService: AuthenticationService
  ) {}
  ngOnInit(): void {
    this.refreshTopCars()
  }


  refreshTopCars() {
    this.carService.getTopCars().subscribe(
      response => {
        console.log(response)
        this.cars = response;
      }
    )
  }

  handleJWTAuthLogin() {
    this.authService.executeJWTAuthService(this.username, this.password).subscribe(
      data => {
        console.log(data)
        this.router.navigate(["welcome"])
        this.invalidLogin = false
      },
      error => {
        console.log(error)
        this.invalidLogin = true
      }
    )
  }
}

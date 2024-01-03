import {Component, OnInit} from '@angular/core';
import { CommonModule } from '@angular/common';
import {Router} from "@angular/router";
import { FormsModule } from '@angular/forms';
import {AuthenticationService} from "../../services/authServices/authentication.service";

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {


  username = "AlexKushyn"
  password = ""
  errorMessage = "Invalid Credentials"
  invalidLogin = false

  constructor(
    private router: Router,
   // private hardcodedAuthenticationService: HardcodedAuthenticationService,
    private authService: AuthenticationService
  ) { }
  ngOnInit(): void {

  }
  handleLogin() {
    /*if (this.hardcodedAuthenticationService.authenticate(this.username, this.password)) {
      this.router.navigate(["welcome", this.username])
      this.invalidLogin = false
    }
    else {
      this.invalidLogin = true
    }*/
  }

  handleBasicAuthLogin() {
   /* this.basicAuthService.executeBasicAuthService(this.username, this.password).subscribe(
      data => {
        console.log(data)
        this.router.navigate(["welcome", this.username])
        this.invalidLogin = false
      },
      error => {
        console.log(error)
        this.invalidLogin = true
      }
    )*/
  }

  handleJWTAuthLogin() {
    this.authService.executeJWTAuthService(this.username, this.password).subscribe(
      data => {
        console.log(data)
        this.router.navigate(["welcome", this.username])
        this.invalidLogin = false
      },
      error => {
        console.log(error)
        this.invalidLogin = true
      }
    )
  }

}

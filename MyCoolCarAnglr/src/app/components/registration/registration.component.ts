import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {NewUser} from "../../models/newUser";
import {UserService} from "../../services/user.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-registration',
  standalone: true,
  imports: [CommonModule, FormsModule, ReactiveFormsModule],
  templateUrl: './registration.component.html',
  styleUrls: ['./registration.component.css']
})
export class RegistrationComponent {

  constructor(
    private router: Router,
    private userService: UserService
  ) {
  }
  user: NewUser = new NewUser('', '', '', '', '');



  registerNewUser() {
    console.log("register user")
    console.log(this.user)
    this.userService.registerNewUser(this.user).subscribe(
      data => {
        console.log(data)
        this.router.navigate(["registration/confirm"])
      }
    )
  }

}

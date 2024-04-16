import {Component} from '@angular/core';
import {CommonModule} from '@angular/common';
import {FormsModule, NgForm, ReactiveFormsModule} from "@angular/forms";
import {Router} from "@angular/router";
import {Store} from "@ngrx/store";
import * as fromAuth from "../login/store/auth.reducer";
import * as AuthActions from '../login/store/auth.actions'

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
    private store: Store<fromAuth.State>
  ) {
  }


  registerNewUser(registrationForm: NgForm) {
    if (!registrationForm.valid) {
      return;
    }
    const firstName = registrationForm.value.firstName;
    const lastName = registrationForm.value.lastName;
    const email = registrationForm.value.email;
    const password = registrationForm.value.password;
    const matchingPassword = registrationForm.value.matchingPassword;
    this.store.dispatch(new AuthActions.RegistrationStart(
      {
        firstName: firstName,
        lastName: lastName,
        email: email,
        password: password,
        matchingPassword: matchingPassword
      }
    ))
  }

}

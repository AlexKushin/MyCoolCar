import {Component, OnInit} from '@angular/core';
import {CommonModule} from '@angular/common';
import {FormControl, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {Store} from "@ngrx/store";
import * as fromApp from "../../../store/app.reducer";
import * as AuthActions from '../../login/store/auth.actions'

@Component({
  selector: 'app-password-reset',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './password-reset.component.html',
  styleUrls: ['./password-reset.component.css']
})


export class PasswordResetComponent implements OnInit {


  resetPasswordForm: FormGroup;
  email: string = '';

  constructor(private store: Store<fromApp.AppState>) {
  }

  ngOnInit(): void {
    this.initForm()
  }


  onSubmit() {
    if (this.resetPasswordForm) {
      let email = this.resetPasswordForm.get('email').value
      this.store.dispatch(new AuthActions.PasswordReset(email))
    }
  }

  initForm() {
    let email = this.email;
    this.resetPasswordForm = new FormGroup({
      'email': new FormControl(email, Validators.required)
    });
  }
}

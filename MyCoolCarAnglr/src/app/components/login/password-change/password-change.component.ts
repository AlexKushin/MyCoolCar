import {Component, OnInit} from '@angular/core';
import {CommonModule} from '@angular/common';
import {FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators} from "@angular/forms";
import {ActivatedRoute} from "@angular/router";
import {Store} from "@ngrx/store";
import * as fromApp from "../../../store/app.reducer";
import * as AuthActions from '../../login/store/auth.actions'


@Component({
  selector: 'app-password-change',
  standalone: true,
  imports: [CommonModule, FormsModule, ReactiveFormsModule],
  templateUrl: './password-change.component.html',
  styleUrls: ['./password-change.component.css']
})
export class PasswordChangeComponent implements OnInit {

  newPasswordForm: FormGroup;
  token: string = '';

  constructor(private route: ActivatedRoute,
              private store: Store<fromApp.AppState>) {
  }


  ngOnInit(): void {

    this.route.queryParams.subscribe(params => {
      this.token = params['token'];
      // Now you can use the 'token' in your component logic
      console.log(this.token);
    });
    this.initForm();
  }

  onSubmit() {
    if (this.newPasswordForm) {
      this.store.dispatch(new AuthActions.PasswordChange(this.newPasswordForm.value))
      console.log('token= ' + this.newPasswordForm.value)
    }
  }


  initForm() {
    let token = this.token;
    let password = '';
    let matchingPassword = '';
    this.newPasswordForm = new FormGroup({
      'token': new FormControl(token, Validators.required),
      'password': new FormControl(password, Validators.required),
      'matchingPassword': new FormControl(matchingPassword, Validators.required),
    });
  }

}

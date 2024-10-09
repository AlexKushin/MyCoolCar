import {Component, OnInit} from '@angular/core';
import {CommonModule} from '@angular/common';
import {FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators} from "@angular/forms";
import {ActivatedRoute} from "@angular/router";
import {Store} from "@ngrx/store";
import * as fromApp from "../../../store/app.reducer";
import * as AuthActions from '../../login/store/auth.actions'

@Component({
  selector: 'app-confirm-registration',
  standalone: true,
  imports: [CommonModule, FormsModule, ReactiveFormsModule],
  templateUrl: './confirm-registration.component.html',
  styleUrls: ['./confirm-registration.component.css']
})
export class ConfirmRegistrationComponent implements OnInit {

  confirmRegistrationForm: FormGroup;
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
    if (this.confirmRegistrationForm) {
      let tokenCode = this.confirmRegistrationForm.get('tokenCode').value
      this.store.dispatch(new AuthActions.RegistrationConfirmStart(tokenCode))
    }
  }


  initForm() {
    let tokenCode = this.token;
    this.confirmRegistrationForm = new FormGroup({
      'tokenCode': new FormControl(tokenCode, Validators.required)
    });
  }
}

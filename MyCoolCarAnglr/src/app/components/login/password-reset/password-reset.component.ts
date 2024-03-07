import {Component, OnInit} from '@angular/core';
import {CommonModule} from '@angular/common';
import {FormControl, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {Router} from "@angular/router";
import {UserService} from "../../../services/user.service";

@Component({
  selector: 'app-password-reset',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './password-reset.component.html',
  styleUrls: ['./password-reset.component.css']
})
export class PasswordResetComponent implements OnInit {


  // @ts-ignore
  resetPasswordForm: FormGroup;
  email: string = '';

  constructor(private userService: UserService,
              private router: Router) {
  }

  ngOnInit(): void {
    this.initForm()
  }


  onSubmit() {
    if (this.resetPasswordForm) {
      // @ts-ignore
      let email = this.resetPasswordForm.get('email').value
      console.log('email= ' + email)
      this.userService.resetPassword(email).subscribe(result => {
          console.log(result.message)
          console.log('result= ' + result.error)
          this.router.navigate(['password/change'])
        },
        error => {
          console.log('Error: ' + error.message)
          this.router.navigate(['login'])
        });

      console.log('email= ' + email)
    }
  }

  initForm() {
    let email = this.email;
    this.resetPasswordForm = new FormGroup({
      'email': new FormControl(email, Validators.required)
    });
  }
}

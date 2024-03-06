import {Component, OnInit} from '@angular/core';
import {CommonModule} from '@angular/common';
import {FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators} from "@angular/forms";
import {ActivatedRoute, Router} from "@angular/router";
import {UserService} from "../../../services/user.service";

@Component({
  selector: 'app-password-change',
  standalone: true,
  imports: [CommonModule, FormsModule, ReactiveFormsModule],
  templateUrl: './password-change.component.html',
  styleUrls: ['./password-change.component.css']
})
export class PasswordChangeComponent implements OnInit {

  // @ts-ignore
  newPasswordForm: FormGroup;
  token: string = '';

  constructor(private route: ActivatedRoute,
              private userService: UserService,
              private router: Router) {
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
    let formData: any = new FormData();
    if (this.newPasswordForm) {
      // @ts-ignore
      let token = this.newPasswordForm.get('token').value
      console.log('token= ' + token)
      // @ts-ignore
      let password = this.newPasswordForm.get('password').value
      console.log('password= ' + password)
      // @ts-ignore
      let matchingPassword = this.newPasswordForm.get('matchingPassword').value
      console.log('passwordConfirm= ' + matchingPassword)
      this.userService.updatePassword(this.newPasswordForm.value).subscribe(result => {
          console.log(result.message)
          this.router.navigate(['login'])
        },
        error => {
          console.log(error.message)
          this.router.navigate(['login'])
        });
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

import {Component, OnInit} from '@angular/core';
import { CommonModule } from '@angular/common';
import {FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators} from "@angular/forms";
import {ActivatedRoute, Router} from "@angular/router";
import {UserService} from "../../../services/user.service";

@Component({
  selector: 'app-confirm-registration',
  standalone: true,
  imports: [CommonModule, FormsModule, ReactiveFormsModule],
  templateUrl: './confirm-registration.component.html',
  styleUrls: ['./confirm-registration.component.css']
})
export class ConfirmRegistrationComponent implements OnInit{

  // @ts-ignore
  confirmRegistrationForm: FormGroup;
  token:string = '';


  constructor(private route: ActivatedRoute,
              private userService: UserService,
              private router: Router) { }


  ngOnInit(): void {

    this.route.queryParams.subscribe(params => {
      this.token = params['token'];
      // Now you can use the 'token' in your component logic
      console.log(this.token);
    });
    this.initForm();
  }

  onSubmit(){
    let formData: any = new FormData();
    if(this.confirmRegistrationForm) {
     // @ts-ignore
      let tokenCode =  this.confirmRegistrationForm.get('tokenCode').value
      this.userService.confirmRegistration(tokenCode).subscribe(result =>{
        console.log(result.message)
        console.log('result= ' + result.error)
        this.router.navigate(['login'])
      });
      console.log('token= '+ tokenCode)
    }
  }


  initForm() {
    let tokenCode = this.token;
    this.confirmRegistrationForm = new FormGroup({
      'tokenCode': new FormControl(tokenCode, Validators.required)
    });
  }
}

import { Injectable } from '@angular/core';
import {Car} from "../models/car";
import {API_URL} from "../app.constants";
import {HttpClient} from "@angular/common/http";
import {NewUser} from "../models/newUser";
import {User} from "../models/user";

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(
    private http: HttpClient
  ) { }

  registerNewUser(newUser: NewUser){
    console.log(newUser)
    return this.http.post(`${API_URL}/api/user/registration`, newUser);
  }
  getCurrentUser(){
    return this.http.get(`${API_URL}/api/me`);
  }
}

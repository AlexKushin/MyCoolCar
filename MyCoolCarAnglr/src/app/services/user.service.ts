import { Injectable } from '@angular/core';
import {Car} from "../models/car";
import {API_URL} from "../app.constants";
import {HttpClient, HttpRequest} from "@angular/common/http";
import {NewUser} from "../models/newUser";
import {User} from "../models/user";
import {Observable} from "rxjs";
import {Response} from "../models/response";

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

  confirmRegistration(token: String):Observable<Response>{
    return this.http.get<Response>(`${API_URL}/api/registration/confirm?token=`+token);
  }
}

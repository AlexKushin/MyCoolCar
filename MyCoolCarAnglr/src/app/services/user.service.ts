import {Injectable} from '@angular/core';
import {API_URL} from "../app.constants";
import {HttpClient} from "@angular/common/http";
import {NewUser} from "../models/newUser";
import {Observable} from "rxjs";
import {Response} from "../models/response";
import {NewPassword} from "../models/new-password";
import {User} from "../models/user";

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(
    private http: HttpClient
  ) {
  }

  registerNewUser(newUser: NewUser) {
    console.log(newUser)
    return this.http.post(`${API_URL}/api/user/registration`, newUser);
  }

  getCurrentUser() {
    return  this.http.get<User>(`${API_URL}/api/me`);
  }

  confirmRegistration(token: String): Observable<Response> {
    return this.http.get<Response>(`${API_URL}/api/registration/confirm?token=` + token);
  }

  resetPassword(email: String): Observable<Response> {
    return this.http.post<Response>(`${API_URL}/api/user/resetPassword?email=` + email, null);
  }

  updatePassword(password: NewPassword): Observable<Response> {
    return this.http.post<Response>(`${API_URL}/api/user/savePassword`, password);
  }
}

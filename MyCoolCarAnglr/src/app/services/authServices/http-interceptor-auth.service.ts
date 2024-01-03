import { Injectable } from '@angular/core';
import {HttpHandler, HttpInterceptor, HttpRequest} from "@angular/common/http";
import {AuthenticationService} from "./authentication.service";

@Injectable({
  providedIn: 'root'
})
export class HttpInterceptorAuthService  implements HttpInterceptor {

  constructor(
    private basicAuthService: AuthenticationService
  ) { }

  intercept(req: HttpRequest<any>, next: HttpHandler) {
    // let username = "AlexKushyn";
    // let password = "password";
    // let basicAuthHeaderString = "Basic " + window.btoa(username + ":" + password)
    let basicAuthHeaderString = this.basicAuthService.getAuthenticatedToken();
    let username = this.basicAuthService.getAuthenticatedUser();

    if (basicAuthHeaderString && username) {
      req = req.clone({
        setHeaders: {
          Authorization: basicAuthHeaderString
        }
      })
    }
    return next.handle(req);
  }

}

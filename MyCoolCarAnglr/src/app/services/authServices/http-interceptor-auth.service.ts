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
    let basicAuthHeaderString = this.basicAuthService.getAuthenticatedToken();
    let username = this.basicAuthService.getAuthenticatedUser();
    if (basicAuthHeaderString && username) {
      req = req.clone({
        setHeaders: {
          Authorization: basicAuthHeaderString
        }
      })
    }
    req = req.clone({setParams:{
      local: 'pl_PL'
      }})
    return next.handle(req);
  }

}

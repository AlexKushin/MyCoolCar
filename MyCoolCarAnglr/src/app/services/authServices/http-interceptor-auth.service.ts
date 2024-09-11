import {Injectable} from '@angular/core';
import {HttpHandler, HttpInterceptor, HttpRequest} from "@angular/common/http";

export const TOKEN = "token"
export const AUTHENTICATED_USER = "authenticatedUser"

@Injectable({
  providedIn: 'root'
})
export class HttpInterceptorAuthService implements HttpInterceptor {

  constructor() {
  }

  intercept(req: HttpRequest<any>, next: HttpHandler) {
    let basicAuthHeaderString = this.getAuthenticatedToken();
    let username = this.getAuthenticatedUser();
    if (basicAuthHeaderString && username) {
      req = req.clone({
        setHeaders: {
          Authorization: basicAuthHeaderString
        }
      })
    }
    req = req.clone({
      setParams: {
        local: 'pl_PL'
      }
    })
    return next.handle(req);
  }


  private getAuthenticatedUser() {
    return sessionStorage.getItem(AUTHENTICATED_USER)
  }

  private getAuthenticatedToken() {
    if (this.getAuthenticatedUser()) {
      return sessionStorage.getItem(TOKEN)
    }
    return ''
  }

}

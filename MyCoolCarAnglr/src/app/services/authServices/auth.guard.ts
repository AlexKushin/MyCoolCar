import {CanActivateFn, Router} from '@angular/router';
import {inject} from "@angular/core";
import {AuthenticationService} from "./authentication.service";

export const authGuard: CanActivateFn = (route, state) => {


  const router = inject(Router);
  const authService = inject(AuthenticationService);
  if (authService.isUserLoggedIn()) {
    return true;
  }
  router.navigate(['login']);
  return false;
};

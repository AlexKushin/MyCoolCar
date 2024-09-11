import {CanActivateFn, Router} from '@angular/router';
import {inject} from "@angular/core";
import {Store} from "@ngrx/store";
import * as fromAuth from "../../components/login/store/auth.reducer";
import {map} from "rxjs";
import {User} from "../../models/user";
import {take} from "rxjs/operators";


export const authGuard: CanActivateFn = (route, state) => {
  const router = inject(Router);
  const store = inject(Store<{ auth: fromAuth.State }>);

  return store.select('auth').pipe(take(1),
    map(authState => {
      console.log("AUTH GUARD authState.user:")
      console.log(authState.user)
      return authState.user;
    }),
    map((user: User) => {
      const isAuth = !!user;
      if (isAuth) {
        console.log("AUTH GUARD: true")
        return true;
      }
      console.log("AUTH GUARD: false")
      router.navigate(['login']);
      return false;
    }));
};

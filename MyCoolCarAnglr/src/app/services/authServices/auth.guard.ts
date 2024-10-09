import {CanActivateFn, Router} from '@angular/router';
import {inject} from "@angular/core";
import {Store} from "@ngrx/store";
import {map} from "rxjs";
import {User} from "../../models/user";
import {take} from "rxjs/operators";
import * as fromApp from "../../store/app.reducer";


export const authGuard: CanActivateFn = (route, state) => {
  const router = inject(Router);
  const store = inject(Store<fromApp.AppState>);

  return store.select('auth').pipe(take(1),
    map(authState => {
      return authState.user;
    }),
    map((user: User) => {
      const isAuth = !!user;
      if (isAuth) {
        return true;
      }
      router.navigate(['login']);
      return false;
    }));
};

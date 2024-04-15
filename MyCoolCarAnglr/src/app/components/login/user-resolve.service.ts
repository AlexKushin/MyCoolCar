import {Injectable} from "@angular/core";
import {Actions, ofType} from "@ngrx/effects";
import {ActivatedRouteSnapshot, Resolve, RouterStateSnapshot} from "@angular/router";
import {User} from "../../models/user";
import * as fromAuth from "./store/auth.reducer";
import {Store} from "@ngrx/store";
import {map, of, take} from "rxjs";
import {switchMap} from "rxjs/operators";
import * as AuthActions from './store/auth.actions'

@Injectable({providedIn: 'root'})
export class UserResolverService implements Resolve<User> {

  constructor(
    private store: Store<{ auth: fromAuth.State }>,
    private actions$: Actions
  ) {
  }

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
    return this.store.select('auth').pipe(
      take(1),
      map(userState => {
        return userState.user;
      }),
      switchMap(user => {
        if (!user) {
          this.store.dispatch(new AuthActions.AutoLogin);
          return this.actions$.pipe(
            ofType(AuthActions.AUTHENTICATED_USER),
            take(1)
          );
        } else {
          return of(user);
        }
      })
    );
  }

}

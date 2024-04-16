import {Injectable} from "@angular/core";
import {ActivatedRouteSnapshot, Resolve, RouterStateSnapshot} from "@angular/router";
import {Car} from "../../models/car";
import {Actions, ofType} from "@ngrx/effects";
import {map, switchMap, take} from "rxjs/operators";
import {Store} from "@ngrx/store";
import * as fromUserCars from "./store/cars.reducer";
import * as UserCarsActions from './store/cars.actions'

import {of} from "rxjs";

@Injectable({providedIn: 'root'})
export class UserCarsResolverService implements Resolve<Car[]> {

  constructor(
    private store: Store<{ userCarsState: fromUserCars.State }>,
    private actions$: Actions
  ) {
  }

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
    return this.store.select('userCarsState').pipe(
      take(1),
      map(userCarsState => {
        return userCarsState.userCars;
      }),
      switchMap(userCars => {
        if (userCars.length === 0) {
          this.store.dispatch(new UserCarsActions.FetchUserCars());
          return this.actions$.pipe(
            ofType(UserCarsActions.SET_CARS),
            take(1)
          );
        } else {
          return of(userCars);
        }
      })
    );
  }
}

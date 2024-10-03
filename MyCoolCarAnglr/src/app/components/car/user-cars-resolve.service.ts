import {Injectable} from "@angular/core";
import {ActivatedRouteSnapshot, Resolve, RouterStateSnapshot} from "@angular/router";
import {Car} from "../../models/car";
import {Actions, ofType} from "@ngrx/effects";
import {map, switchMap, take} from "rxjs/operators";
import {Store} from "@ngrx/store";
import {of} from "rxjs";
import * as fromApp from '../../store/app.reducer';
import * as UserCarsActions from './store/cars.actions'

@Injectable({providedIn: 'root'})
export class UserCarsResolverService implements Resolve<Car[]> {

  constructor(
    private store: Store<fromApp.AppState>,
    private actions$: Actions
  ) {
  }

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
    return this.store.select('userCars').pipe(
      take(1),
      map((userCarsState) => {
        console.log("UserCarsResolverService")
        console.log(userCarsState)
        return userCarsState.userCars;
      }),
      switchMap(userCars => {
        if (userCars.length === 0) {
          console.log("resolver work")
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

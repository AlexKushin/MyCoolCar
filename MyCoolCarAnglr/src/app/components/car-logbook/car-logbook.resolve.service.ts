import {Injectable} from "@angular/core";
import {ActivatedRouteSnapshot, Resolve, RouterStateSnapshot} from "@angular/router";
import {CarLogbook} from "../../models/carLogbook";
import {Store} from "@ngrx/store";
import * as fromCarLogbook from "./store/car-logbook.reducer";
import * as CarLogbookActions from './store/car-logbook.actions'
import {Actions, ofType} from "@ngrx/effects";
import {map, switchMap, take} from "rxjs/operators";
import {of} from "rxjs";

@Injectable({providedIn: 'root'})
export class CarLogbookResolverService implements Resolve<CarLogbook> {


  constructor(
    private store: Store<{ carLogbookState: fromCarLogbook.State }>,
    private actions$: Actions
  ) {
  }


  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {

    const carId = +route.paramMap.get('id');
    return this.store.select('carLogbookState').pipe(
      take(1),
      map((carLogbookState: fromCarLogbook.State) => {
        console.log("CarLogbookResolverService")
        console.log(carLogbookState)
        return carLogbookState.carLogbook;
      }),
      switchMap(carLogbook => {
        if (carLogbook.id === -1 || carLogbook.carId !== carId) {
          console.log("CarLogbook resolver works")

          this.store.dispatch(new CarLogbookActions.FetchCarLogbook(carId));
          return this.actions$.pipe(
            ofType(CarLogbookActions.SET_CAR_LOGBOOK),
            take(1)
          );
        } else {
          return of(carLogbook);
        }
      })
    );
  }
}

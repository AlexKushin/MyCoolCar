import {Injectable} from "@angular/core";
import {ActivatedRouteSnapshot, Resolve, RouterStateSnapshot} from "@angular/router";
import {CarLogbook} from "../../models/carLogbook";
import {Store} from "@ngrx/store";
import {Actions, ofType} from "@ngrx/effects";
import {map, switchMap, take} from "rxjs/operators";
import {of} from "rxjs";
import * as fromCarLogbook from "./store/car-logbook.reducer";
import * as CarLogbookActions from './store/car-logbook.actions'
import * as fromApp from '../../store/app.reducer';

@Injectable({providedIn: 'root'})
export class CarLogbookResolverService implements Resolve<CarLogbook> {


  constructor(
    private store: Store<fromApp.AppState>,
    private actions$: Actions
  ) {
  }


  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {

    const carId = +route.paramMap.get('id');
    return this.store.select('carLogbook').pipe(
      take(1),
      map((carLogbookState: fromCarLogbook.State) => {
        return carLogbookState.carLogbook;
      }),
      switchMap(carLogbook => {
        if (carLogbook.id === -1 || carLogbook.carId !== carId) {
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

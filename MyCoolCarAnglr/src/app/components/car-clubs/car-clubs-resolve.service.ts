import {Injectable} from "@angular/core";
import {ActivatedRouteSnapshot, Resolve, RouterStateSnapshot} from "@angular/router";
import {Store} from "@ngrx/store";
import * as fromApp from "../../store/app.reducer";
import * as CarClubsActions from './store/car-club.actions'
import {Actions, ofType} from "@ngrx/effects";
import {map, switchMap, take} from "rxjs/operators";
import {of} from "rxjs";
import {CarClub} from "../../models/carClub";

@Injectable({providedIn: 'root'})
export class CarClubsResolverService implements Resolve<CarClub[]> {

  constructor(
    private store: Store<fromApp.AppState>,
    private actions$: Actions
  ) {
  }

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
    return this.store.select('carClubs').pipe(
      take(1),
      map((carClubsState) => {
        console.log("CarClubsResolverService")
        console.log(carClubsState)
        return carClubsState.carClubs;
      }),
      switchMap(carClubs => {
        if (carClubs.length === 0) {
          console.log("resolver work")
          this.store.dispatch(new CarClubsActions.FetchCarClubs());
          return this.actions$.pipe(
            ofType(CarClubsActions.SET_CAR_CLUBS),
            take(1)
          );
        } else {
          return of(carClubs);
        }
      })
    );
  }


}

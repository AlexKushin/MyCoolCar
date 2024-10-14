import {map} from "rxjs";
import {Injectable} from "@angular/core";
import {Actions, createEffect, ofType} from "@ngrx/effects";
import { switchMap} from "rxjs/operators";
import {API_URL} from "../../../app.constants";
import {HttpClient} from "@angular/common/http";
import {Router} from "@angular/router";
import {Store} from "@ngrx/store";
import * as fromApp from "../../../store/app.reducer";
import * as CarClubsActions from "./car-club.actions"
import {CarClub} from "../../../models/carClub";

@Injectable()
export class CarClubEffects {

  fetchCarClubs = createEffect(() => this.actions$.pipe(
    ofType(CarClubsActions.FETCH_CAR_CLUBS),
    switchMap(() => {
      return this.http.get<CarClub[]>(`${API_URL}/api/car_clubs`)
    }),
    map(carClubs => {
      return carClubs.map(carClub => {
        return {...carClub};
      });
    }),
    map(carClubs => {
      return new CarClubsActions.SetCarClubs(carClubs);
    })
  ));

  fetchUserCarClubs = createEffect(() => this.actions$.pipe(
    ofType(CarClubsActions.FETCH_USER_CAR_CLUBS),
    switchMap(() => {
      return this.http.get<CarClub[]>(`${API_URL}/api/car_clubs/my`)
    }),
    map(userCarClubs => {
      return userCarClubs.map(userCarClub => {
        return {...userCarClub};
      });
    }),
    map(userCarClubs => {
      return new CarClubsActions.SetUserCarClubs(userCarClubs);
    })
  ));

  constructor(
    private actions$: Actions,
    private http: HttpClient,
    private router: Router,
    private store: Store<fromApp.AppState>
  ) {
  }
}

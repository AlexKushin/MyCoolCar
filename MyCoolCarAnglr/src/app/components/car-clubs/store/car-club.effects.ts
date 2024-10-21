import {map} from "rxjs";
import {Injectable} from "@angular/core";
import {Actions, createEffect, ofType} from "@ngrx/effects";
import {switchMap, tap} from "rxjs/operators";
import {API_URL} from "../../../app.constants";
import {HttpClient} from "@angular/common/http";
import {Router} from "@angular/router";
import {Store} from "@ngrx/store";
import * as fromApp from "../../../store/app.reducer";
import * as CarClubsActions from "./car-club.actions"
import {CarClub} from "../../../models/carClub";
import {CarClubActions} from "./car-club.actions";

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

  createNewCarClub = createEffect(() => this.actions$.pipe(
    ofType(CarClubsActions.CREATE_CAR_CLUB),
    switchMap((carClub: CarClubsActions.CreateCarClub) => {
      console.log("createNewCarClub EFFECT")
      console.log("carClub.payload:")
      console.log(carClub.payload)
      return this.http.post<CarClub>(`${API_URL}/api/car_clubs/new`, carClub.payload)
    }),
    map(userCarClub => {
      return new CarClubsActions.SetUserCarClub(userCarClub);
    })
  ));


  redirectAfterCarClubManipulating$ = createEffect(
    () =>
      this.actions$.pipe(
        ofType(CarClubsActions.SET_USER_CAR_CLUB),
        tap(() => {
          this.router.navigate(['/car_clubs']);
        })
      ),
    {dispatch: false}
  );

  constructor(
    private actions$: Actions,
    private http: HttpClient,
    private router: Router,
    private store: Store<fromApp.AppState>
  ) {
  }
}

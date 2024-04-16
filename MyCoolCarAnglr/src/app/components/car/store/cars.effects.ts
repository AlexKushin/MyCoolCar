import {Injectable} from "@angular/core";
import {Actions,createEffect, ofType} from "@ngrx/effects";
import {switchMap, withLatestFrom} from "rxjs/operators";
import {map} from "rxjs";
import {HttpClient} from "@angular/common/http";
import { Store } from "@ngrx/store";
import * as fromUserCars from './cars.reducer'
import * as UserCarsActions from './cars.actions'
import {Car} from "../../../models/car";
import {API_URL} from "../../../app.constants";

@Injectable()
export class UserCarsEffects {

  fetchUserCars = createEffect(() => this.actions$.pipe(
    ofType(UserCarsActions.FETCH_CARS),
    switchMap(() => {
      console.log("UserCarsActions.FETCH_CARS")
      return this.http.get<Car[]>(`${API_URL}/api/cars`)
    }),
    map(userCars => {
      return userCars.map(userCar => {
        return { ...userCar/*, ingredients: userCar.ingredients ? userCar.ingredients : []*/ };
      });
    }),
    map(userCars => {
      console.log("usercars= "+ userCars)
      return new UserCarsActions.SetUserCars(userCars);
    })
  ));

  storeRecipes = createEffect(() => this.actions$.pipe(
    ofType(UserCarsActions.STORE_CARS),
    withLatestFrom(this.store.select('userCars')),
    switchMap(([actionData, userCars]) => {
        return this.http.put('https://recipebook-a429e-default-rtdb.europe-west1.firebasedatabase.app/:recipes.json', userCars
        )
      }
    )
  ), { dispatch: false })

  // @ts-ignore
  constructor(
    private actions$: Actions,
    private http: HttpClient,
    private store: Store<fromUserCars.State>
  ) { }
}

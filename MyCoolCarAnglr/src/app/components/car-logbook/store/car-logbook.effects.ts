

import * as fromCarLogbook from "./car-logbook.reducer";
import * as CarLogbookActions from './car-logbook.actions'
import {Injectable} from "@angular/core";
import {Actions, createEffect, ofType} from "@ngrx/effects";
import {HttpClient} from "@angular/common/http";
import {Router} from "@angular/router";
import {Store} from "@ngrx/store";
import {switchMap} from "rxjs/operators";
import {API_URL} from "../../../app.constants";

import {map} from "rxjs";
import {CarLogbook} from "../../../models/carLogbook";



@Injectable()
export class CarLogbookEffects {
  constructor(
    private actions$: Actions,
    private http: HttpClient,
    private router: Router,
    private store: Store<fromCarLogbook.State>
  ) {
  }



  fetchCarLogbook = createEffect(() => this.actions$.pipe(
    ofType(CarLogbookActions.FETCH_CAR_LOGBOOK),
    switchMap((carId: CarLogbookActions.FetchCarLogbook) => {
      console.log("fetchCarLogbook111")
      console.log(carId.payload)
      return this.http.get<CarLogbook>(`${API_URL}/api/car/${carId.payload}/logbook`)
    }),
    map(carLogbook => {
      console.log("fetch logbook effect")
      console.log(carLogbook)
      return new CarLogbookActions.SetCarLogbook(carLogbook);
    })
  ));
}

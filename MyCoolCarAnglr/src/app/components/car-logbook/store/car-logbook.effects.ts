import * as fromCarLogbook from "./car-logbook.reducer";
import * as CarLogbookActions from './car-logbook.actions'
import {Injectable} from "@angular/core";
import {Actions, createEffect, ofType} from "@ngrx/effects";
import {HttpClient} from "@angular/common/http";
import {Router} from "@angular/router";
import {Store} from "@ngrx/store";
import {switchMap, tap} from "rxjs/operators";
import {API_URL} from "../../../app.constants";
import {map} from "rxjs";
import {CarLogbook} from "../../../models/carLogbook";
import {CarLogbookPost} from "../../../models/carLogbookPost";


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
      return this.http.get<CarLogbook>(`${API_URL}/api/cars/${carId.payload}/logbook`)
    }),
    map(carLogbook => {
      return new CarLogbookActions.SetCarLogbook(carLogbook);
    })
  ));

  addNewCarLogbookPost = createEffect(() => this.actions$.pipe(
    ofType(CarLogbookActions.ADD_NEW_CAR_LOGBOOK_POST),
    switchMap((logbookPost: CarLogbookActions.AddCarLogbookPost) => {

      return this.http.post<CarLogbookPost>(`${API_URL}/api/car-logbook/${logbookPost.payload.carLogbookId}/car-log-posts/new`, logbookPost.payload.logbookPost)
    }),
    map(logbookPost => {
      console.log("NEW LOGBOOK POST")
      console.log(logbookPost)
      return new CarLogbookActions.SetCarLogbookPost(logbookPost);
    })
  ));

  deleteCarLogbookPost = createEffect(() => this.actions$.pipe(
    ofType(CarLogbookActions.DELETE_CAR_LOGBOOK_POST),
    switchMap((carLogBookPostId: CarLogbookActions.DeleteCarLogbookPost) => {
        return this.http.delete(`${API_URL}/api/car-logbook-posts/${carLogBookPostId.payload}`).pipe(
          map(() => {
            this.store.dispatch(new CarLogbookActions.DeleteCarLogbookPostSuccess())
          })
        )
      }
    )
  ), {dispatch: false})

  editCarLogbookPost = createEffect(() => this.actions$.pipe(
    ofType(CarLogbookActions.EDIT_CAR_LOGBOOK_POST),
    switchMap((data: CarLogbookActions.EditCarLogbookPost) => {

      return this.http.put<CarLogbookPost>(`${API_URL}/api/car-logbook-posts/${data.payload.carLogbookId}`,
        data.payload.logbookPost)
    }),
    map(carLogbookPost => {
      console.log('car logbook post after editing')
      console.log(carLogbookPost)
      return new CarLogbookActions.SetEditedCarLogbookPost({id: carLogbookPost.id, carLogbookPost: carLogbookPost});
    })
  ));


  redirectAfterNewCarLogbookPostAdding$ = createEffect(
    () =>
      this.actions$.pipe(
        ofType(CarLogbookActions.SET_NEW_CAR_LOGBOOK_POST),
        tap(() => {
          this.getCarIdAndNavigate()
        })
      ),
    {dispatch: false}
  );

  redirectAfterCarLogbookPostDeleting$ = createEffect(
    () =>
      this.actions$.pipe(
        ofType(CarLogbookActions.DELETE_CAR_LOGBOOK_POST_SUCCESS),
        tap(() => {
          this.getCarIdAndNavigate()
        })
      ),
    {dispatch: false}
  );

  redirectAfterCarLogbookPostEditing$ = createEffect(
    () =>
      this.actions$.pipe(
        ofType(CarLogbookActions.SET_EDITED_CAR_LOGBOOK_POST),
        tap(() => {
          this.getCarIdAndNavigate()
        })
      ),
    {dispatch: false}
  );

  private getCarIdAndNavigate():void{
    const currentRoute = this.router.routerState.snapshot.root;
    let carId: number
    let route = currentRoute;
    while (route.firstChild) {
      route = route.firstChild;
    }
    carId = +route.params['id'];
    if (carId) {
      this.router.navigate([`/cars/${carId}`]);
    }
  }
}

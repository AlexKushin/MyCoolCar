import {Injectable} from "@angular/core";
import {Actions, createEffect, ofType} from "@ngrx/effects";
import {catchError, switchMap, tap, withLatestFrom} from "rxjs/operators";
import {map, of} from "rxjs";
import {HttpClient} from "@angular/common/http";
import {Store} from "@ngrx/store";
import * as fromUserCars from './cars.reducer'
import * as UserCarsActions from './cars.actions'
import {Car} from "../../../models/car";
import {API_URL} from "../../../app.constants";
import {Router} from "@angular/router";

const handleError = (errorRes: any) => {

  let errorMessage = 'An unknown error occurred!';
  if (!errorRes.error || !errorRes.error.error) {
    return of(new UserCarsActions.UserCarFail(errorMessage));

  }
  switch (errorRes.error.error) {
    case 'EMAIL_EXISTS':
      errorMessage = 'This email exists already';
      break;
    case 'Bad credentials':
      //errorMessage = 'Email or password is not correct.';
      errorMessage = errorRes.error.message;
      break;
  }
  console.log(errorMessage)

  return of(new UserCarsActions.UserCarFail(errorMessage))
};

@Injectable()
export class UserCarsEffects {

  fetchUserCars = createEffect(() => this.actions$.pipe(
    ofType(UserCarsActions.FETCH_CARS),
    switchMap(() => {
      return this.http.get<Car[]>(`${API_URL}/api/cars`)
    }),
    map(userCars => {
      return userCars.map(userCar => {
        return {...userCar};
      });
    }),
    map(userCars => {
      return new UserCarsActions.SetUserCars(userCars);
    })
  ));

  deleteUserCar = createEffect(() => this.actions$.pipe(
    ofType(UserCarsActions.DELETE_CAR),
    switchMap((carId: UserCarsActions.DeleteUserCar) => {
        return this.http.delete(`${API_URL}/api/cars/${carId.payload}`).pipe(
          map(() => {
            this.store.dispatch(new UserCarsActions.DeleteUserCarSuccess())
          }),
          catchError(errorRes => {
            return handleError(errorRes)
          })
        )
      }
    )
  ), {dispatch: false})


  addNewUserCar = createEffect(() => this.actions$.pipe(
    ofType(UserCarsActions.ADD_NEW_USER_CAR),
    switchMap((car: UserCarsActions.AddUserCar) => {
      console.log("new user car" + car)
      console.log("new user car.payload" + car.payload)
      return this.http.post<Car>(`${API_URL}/api/cars/new`, car.payload)
    }),
    map(userCar => {
      console.log("new  user car")
      console.log(userCar)
      return new UserCarsActions.SetUserCar(userCar);
    })
  ));

  updateUserCar = createEffect(() => this.actions$.pipe(
    ofType(UserCarsActions.UPDATE_CAR),
    switchMap((data: UserCarsActions.UpdateUserCar) => {
      console.log("update car data.payload.id")
      console.log(data.payload.id)
      console.log("update car data.payload.userCar")
      console.log(data.payload.userCar)

      return this.http.put<Car>(`${API_URL}/api/cars/${data.payload.id}`, data.payload.userCar,
        {
          headers: {
            'Content-Type': 'application/json'
          }
        })
    }),
    map(userCar => {
      console.log("updated user car" + userCar)
      return new UserCarsActions.UpdateUserCar({id: userCar.id, userCar: userCar});
    })
  ));

  redirectAfterCarManipulating = createEffect(
    () =>
      this.actions$.pipe(
        ofType(UserCarsActions.SET_USER_CAR),
        tap(() => {
          this.router.navigate(['/cars']).then(() => {
            // Handle any post-navigation logic here
          }).catch(error => {
            // Handle navigation error
          });
        })
      ),
    {dispatch: false}
  );

  redirectAfterCarDeleting = createEffect(
    () =>
      this.actions$.pipe(
        ofType(UserCarsActions.DELETE_CAR_SUCCESS),
        tap(() => {
          this.router.navigate(['/cars']).then(() => {
            // Handle any post-navigation logic here
          }).catch(error => {
            // Handle navigation error
          });
        })
      ),
    {dispatch: false}
  );

  redirectAfterCarUpdating = createEffect(
    () =>
      this.actions$.pipe(
        ofType(UserCarsActions.UPDATE_CAR),
        tap(() => {
          this.router.navigate(['/cars']).then(() => {
            // Handle any post-navigation logic here
          }).catch(error => {
            // Handle navigation error
          });
        })
      ),
    {dispatch: false}
  );


  constructor(
    private actions$: Actions,
    private http: HttpClient,
    private router: Router,
    private store: Store<fromUserCars.State>
  ) {
  }
}

import {Injectable} from "@angular/core";
import {Actions, createEffect, ofType} from "@ngrx/effects";
import * as AuthActions from './auth.actions'
import {catchError, map, switchMap, tap} from "rxjs/operators";
import {of} from "rxjs";
import {HttpClient} from "@angular/common/http";
import {Router} from "@angular/router";
import {AUTHENTICATED_USER, AuthenticationService, TOKEN} from "../../../services/authServices/authentication.service";

import {API_URL} from "../../../app.constants";
import {User} from "../../../models/user";

export interface AuthResponseData {
  token: string
}

const handleAuthentication = (email: string, token: string) => {
  //const expirationDate = new Date(new Date().getTime() + expiresIn * 1000);
  //const user = new User(email, userId, token, expirationDate);
  sessionStorage.setItem(AUTHENTICATED_USER, email);
  //sessionStorage.setItem(TOKEN, token);
  sessionStorage.setItem(TOKEN, `Bearer ${token}`);
  //getMe and insert to AuthActions.AuthenticateSuccess

  return new AuthActions.AuthenticateSuccess();
};


const handleError = (errorRes: any) => {

  let errorMessage = 'An unknown error occurred!';
  if (!errorRes.error || !errorRes.error.error) {
    return of(new AuthActions.AuthenticateFail(errorMessage));

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
  console.log(errorMessage)
  console.log(errorMessage)
  console.log(errorMessage)
  return of(new AuthActions.AuthenticateFail(errorMessage))
};

const getUser = (resData: User) => {
  return new AuthActions.AuthenticatedUser(
    {
      id: resData.id,
      ban: resData.ban,
      firstName: resData.firstName,
      lastName: resData.lastName,
      email: resData.email,
      enabled: resData.enabled,
      userCars: resData.userCars
    });
}

@Injectable()
export class AuthEffects {
  constructor(
    private actions$: Actions,
    private http: HttpClient,
    private router: Router,
    private authService: AuthenticationService) {
  }


  authLogin = createEffect(() =>
    this.actions$.pipe(
      ofType(AuthActions.LOGIN_START),
      switchMap((authData: AuthActions.LoginStart) => {
        return this.http
          .post<AuthResponseData>(`${API_URL}/authenticate`,
            {
              username: authData.payload.email,
              password: authData.payload.password,
            }
          )
          .pipe(
            map((resData) => {
              return handleAuthentication(authData.payload.email, resData.token);
            }),
            catchError(errorRes => {
              return handleError(errorRes)
            })
          );
      })
    )
  );


  getAuthenticatedUser = createEffect(() =>
    this.actions$.pipe(
      ofType(AuthActions.AUTHENTICATE_SUCCESS),
      switchMap(() => {
        return this.http.get<User>(`${API_URL}/api/me`)
          .pipe(
            map((resData: User) => {
              return getUser(resData)
            }),
            catchError(errorRes => {
              return handleError(errorRes)
            })
          );
      })
    )
  );

  registration = createEffect(() =>
      this.actions$.pipe(
        ofType(AuthActions.REGISTRATION_START),
        switchMap((regData: AuthActions.RegistrationStart) => {
          return this.http.post(`${API_URL}/api/user/registration`, {
            firstName: regData.payload.firstName,
            lastName: regData.payload.lastName,
            email: regData.payload.email,
            password: regData.payload.password,
            matchingPassword: regData.payload.matchingPassword

          })
            .pipe(
              map(() => {
                  this.router.navigate(['/registration/confirm']);
              }),
              catchError(errorRes => {
                return handleError(errorRes)
              })
            );
        })
      ),
    {dispatch: false}
  );
  authRedirect = createEffect(
    () =>
      this.actions$.pipe(
        ofType(AuthActions.AUTHENTICATE_SUCCESS),
        tap(() => {
          this.router.navigate(['/welcome']);
        })
      ),
    {dispatch: false}
  );

  authLogout = createEffect(
    () =>
      this.actions$.pipe(
        ofType(AuthActions.LOGOUT),
        tap(() => {
          // this.authService.clearLogoutTimer();
          sessionStorage.removeItem(TOKEN)
          sessionStorage.removeItem(AUTHENTICATED_USER)
          this.router.navigate(['/login']);
        })
      ),
    {dispatch: false}
  );

  autoLogin = createEffect(
    () => this.actions$.pipe(
      ofType(AuthActions.AUTO_LOGIN),
      switchMap(() => {
        return this.http.get<User>(`${API_URL}/api/me`)
          .pipe(
            map((resData: User) => {
              return getUser(resData)
            }),
            catchError(errorRes => {
              return handleError(errorRes)
            })
          );
      })
    )
  )

}

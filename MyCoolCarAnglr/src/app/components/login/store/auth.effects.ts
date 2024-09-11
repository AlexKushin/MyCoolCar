import {Injectable} from "@angular/core";
import {Actions, createEffect, ofType} from "@ngrx/effects";
import * as AuthActions from './auth.actions'
import {catchError, map, switchMap, tap, mergeMap} from "rxjs/operators";
import {of} from "rxjs";
import {HttpClient} from "@angular/common/http";
import {Router} from "@angular/router";
import {AUTHENTICATED_USER, TOKEN} from "../../../services/authServices/http-interceptor-auth.service";
import {API_URL} from "../../../app.constants";
import {User} from "../../../models/user";
import {Store} from "@ngrx/store";
import * as fromAuth from "./auth.reducer";

export interface AuthResponseData {
  token: string
}

const handleAuthentication = (email: string, token: string) => {
  sessionStorage.setItem(AUTHENTICATED_USER, email);
  sessionStorage.setItem(TOKEN, `Bearer ${token}`);
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

  return of(new AuthActions.AuthenticateFail(errorMessage))
};

const setUser = (resData: User) => {
  return new AuthActions.SetAuthenticatedUser(resData);
}

@Injectable()
export class AuthEffects {
  constructor(
    private actions$: Actions,
    private http: HttpClient,
    private router: Router,
    private store: Store<{ auth: fromAuth.State }>
  ) {
  }


  private loginRequest(email: string, password: string) {
    return this.http.post<AuthResponseData>(`${API_URL}/api/authenticate`, {
      username: email,
      password: password,
    });
  }

  private fetchAuthenticatedUser() {
    return this.http.get<User>(`${API_URL}/api/me`);
  }

  private handleAuthenticationFlow(email: string, token: string) {
    handleAuthentication(email, token);
    return this.fetchAuthenticatedUser().pipe(
      mergeMap((user: User) => [
        setUser(user), // Dispatch SetAuthenticatedUser action
        new AuthActions.AuthenticateSuccess(), // Dispatch AuthenticateSuccess action
      ]),
      catchError((errorRes) => handleError(errorRes))
    );
  }

  authLogin = createEffect(() =>
    this.actions$.pipe(
      ofType(AuthActions.LOGIN_START),
      switchMap((authData: AuthActions.LoginStart) =>
        this.loginRequest(authData.payload.email, authData.payload.password).pipe(
          tap(resData => {
            setTimeout(() => {
              this.store.dispatch(new AuthActions.Logout());
            }, 3000);
          }),
          switchMap((resData: AuthResponseData) =>
            this.handleAuthenticationFlow(authData.payload.email, resData.token)
          ),
          catchError((errorRes) => handleError(errorRes))
        )
      )
    )
  );

  registration = createEffect(() =>
      this.actions$.pipe(
        ofType(AuthActions.REGISTRATION_START),
        switchMap((regData: AuthActions.RegistrationStart) => {
          return this.http.post(`${API_URL}/api/user/registration`, regData.payload)
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
          this.router.navigate(['/current/welcome']);
        })
      ),
    {dispatch: false}
  );

  authLogout = createEffect(
    () =>
      this.actions$.pipe(
        ofType(AuthActions.LOGOUT),
        tap(() => {
          sessionStorage.removeItem(TOKEN)
          sessionStorage.removeItem(AUTHENTICATED_USER)
          this.router.navigate(['/welcome']);
        })
      ),
    {dispatch: false}
  );
}

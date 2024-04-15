import {Action} from "@ngrx/store";
import {Car} from "../../../models/car";

export const LOGIN_START = '[Auth] Login Start';
export const AUTHENTICATE_SUCCESS = '[Auth] Authenticate Success';
export const AUTHENTICATED_USER = '[Auth] Authenticated User';

export const REGISTRATION_START = '[Auth] Registration Start';
export const AUTHENTICATE_FAIL = '[Auth] Authenticate Fail';

export const AUTO_LOGIN = '[Auth] Auto Login'
export const LOGOUT = '[Auth] Logout';

export class LoginStart implements Action {
  readonly type = LOGIN_START;

  constructor(public payload: { email: string, password: string }) {
  }
}

export class AuthenticateSuccess implements Action {
  readonly type = AUTHENTICATE_SUCCESS;

}

export class AuthenticatedUser implements Action {
  readonly type = AUTHENTICATED_USER;

  constructor(public payload: {
    id: number, ban: boolean, firstName: string,
    lastName: string, email: string, enabled: boolean, userCars: Car[]
  }) {
  }

}

export class RegistrationStart implements Action {
  readonly type = REGISTRATION_START;

  constructor(public payload: {firstName: string, lastName: string,
    email: string, password: string, matchingPassword: string
  }) { }
}

export class AutoLogin implements Action {
  readonly  type = AUTO_LOGIN;

}
export class Logout implements Action {
  readonly type = LOGOUT;
}

export class AuthenticateFail implements Action {
  readonly type = AUTHENTICATE_FAIL;

  constructor(public payload: string) {
  }
}



export type AuthActions =
  | LoginStart
  | AuthenticateSuccess
  | AuthenticateFail
  | AuthenticatedUser
  | RegistrationStart
  | AutoLogin
  | Logout

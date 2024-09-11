import {Action} from "@ngrx/store";
import {User} from "../../../models/user";
import {NewUser} from "../../../models/newUser";

export const LOGIN_START = '[Auth] Login Start';
export const AUTHENTICATE_SUCCESS = '[Auth] Authenticate Success';
export const SET_AUTHENTICATED_USER = '[Auth] Set Authenticated User';

export const REGISTRATION_START = '[Auth] Registration Start';
export const AUTHENTICATE_FAIL = '[Auth] Authenticate Fail';

export const GET_AUTHENTICATED_USER = '[Auth] Get Authenticated User'
export const LOGOUT = '[Auth] Logout';

export class LoginStart implements Action {
  readonly type = LOGIN_START;

  constructor(public payload: { email: string, password: string }) {
  }
}

export class AuthenticateSuccess implements Action {
  readonly type = AUTHENTICATE_SUCCESS;

}

export class SetAuthenticatedUser implements Action {
  readonly type = SET_AUTHENTICATED_USER;

  constructor(public payload: User) {
  }

}

export class RegistrationStart implements Action {
  readonly type = REGISTRATION_START;

  constructor(public payload: NewUser) {
  }
}

export class GetAuthenticatedUser implements Action {
  readonly type = GET_AUTHENTICATED_USER;

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
  | SetAuthenticatedUser
  | RegistrationStart
  | GetAuthenticatedUser
  | Logout

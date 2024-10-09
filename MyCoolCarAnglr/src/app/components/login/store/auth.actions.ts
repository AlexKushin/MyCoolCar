import {Action} from "@ngrx/store";
import {User} from "../../../models/user";
import {NewUser} from "../../../models/newUser";
import {NewPassword} from "../../../models/new-password";

export const LOGIN_START = '[Auth] Login Start';
export const AUTHENTICATE_SUCCESS = '[Auth] Authenticate Success';
export const SET_AUTHENTICATED_USER = '[Auth] Set Authenticated User';

export const REGISTRATION_START = '[Auth] Registration Start';
export const REGISTRATION_START_SUCCESS = '[Auth] Registration Start Success';
export const REGISTRATION_CONFIRM_START = '[Auth] Registration Confirm Start';
export const REGISTRATION_CONFIRM_SUCCESS = '[Auth] Registration Confirm Success';
export const AUTHENTICATE_FAIL = '[Auth] Authenticate Fail';
export const PASSWORD_RESET = '[Auth] Password Reset';
export const PASSWORD_CHANGE = '[Auth] Password Save';

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

export class RegistrationStartSuccess implements Action {
  readonly type = REGISTRATION_START_SUCCESS;

}

export class RegistrationConfirmStart implements Action {
  readonly type = REGISTRATION_CONFIRM_START;

  constructor(public payload: string) {
  }

}

export class PasswordReset implements Action {
  readonly type = PASSWORD_RESET;

  constructor(public payload: string) {
  }

}

export class PasswordChange implements Action {
  readonly type = PASSWORD_CHANGE;

  constructor(public payload: NewPassword) {
  }

}

export class RegistrationConfirmSuccess implements Action {
  readonly type = REGISTRATION_CONFIRM_SUCCESS;

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
  | RegistrationStartSuccess
  | GetAuthenticatedUser
  | Logout
  | RegistrationConfirmStart
  | RegistrationConfirmSuccess
  | PasswordReset
  | PasswordChange

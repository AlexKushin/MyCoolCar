import {User} from "../../../models/user";
import * as  AuthActions from "./auth.actions";

export interface State {
  user: User;
  authError: string;
  loading: boolean;
}


const initialState: State = {

// @ts-ignore
  user: null,
  // @ts-ignore
  authError: null,
  loading: false
}


export function authReducer(state = initialState, action: AuthActions.AuthActions) {
  switch (action.type) {
    case AuthActions.LOGIN_START:
      return {
        ...state,
        authError: null,
        loading: true
      };
    case AuthActions.AUTHENTICATE_FAIL:
      return {
        ...state,
        user: null,
        authError: action.payload,
        loading: false
      };

    case AuthActions.AUTHENTICATED_USER:
      console.log("******************" + action.payload)
      const user = new User(
        action.payload.id,
        action.payload.ban,
        '',
        action.payload.firstName,
        action.payload.lastName,
        action.payload.email,
        [],
        action.payload.enabled
      );
      console.log("user = " + user)
      return {
        ...state,
        authError: null,
        user: user,
        loading: false

      }

    case AuthActions.REGISTRATION_START:
      return {
        ...state,
        authError: null,
        loading: true
      }
    case AuthActions.LOGOUT:
      return {
        ...state,
        user: null
      };
    case AuthActions.AUTHENTICATE_SUCCESS:

    default:
      return state;
  }
}

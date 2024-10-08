import {User} from "../../../models/user";
import * as  AuthActions from "./auth.actions";

export interface State {
  user: User | null;
  authError: string | null;
  loading: boolean;
}


const initialState: State = {
  user: null,
  authError: null,
  loading: false
}


export function authReducer(state: State = initialState, action: AuthActions.AuthActions) {
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

    case AuthActions.SET_AUTHENTICATED_USER:
      const user = new User(
        action.payload.id,
        action.payload.ban,
        '',
        action.payload.firstName,
        action.payload.lastName,
        action.payload.email,
        action.payload.userCars,
        action.payload.enabled
      );
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

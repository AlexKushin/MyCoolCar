
import { LOGOUT } from '../../components/login/store/auth.actions';
import {Action, ActionReducer} from "@ngrx/store";

//https://smtaha512.medium.com/clear-state-in-ngrx-on-logout-87b64e3b3357
export function clearStateMetaReducer<State extends {}>(reducer: ActionReducer<State>): ActionReducer<State> {
  return function clearStateFn(state: State, action: Action) {
    if (action.type === LOGOUT) {
      state = {} as State; // ==> Emptying state here
    }
    return reducer(state, action);
  };
}

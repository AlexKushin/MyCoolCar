import {Action, ActionReducer} from "@ngrx/store";
import {AppState} from "../app.reducer";
import * as HydrationActions from "./hydration.actions";

//https://nils-mehlhorn.de/posts/ngrx-keep-state-refresh/
function isHydrateSuccess(
  action: Action
): action is ReturnType<typeof HydrationActions.hydrateSuccess> {
  return action.type === HydrationActions.hydrateSuccess.type;
}

export const hydrationMetaReducer = (
  reducer: ActionReducer<AppState>
): ActionReducer<AppState> => {
  return (state, action) => {
    if (isHydrateSuccess(action)) {
      return action.state;
    } else {
      return reducer(state, action);
    }
  };
};

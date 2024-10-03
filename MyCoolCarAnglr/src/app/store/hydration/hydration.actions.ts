import {createAction, props} from "@ngrx/store";
import {AppState} from "../app.reducer";


export const hydrate = createAction("[Hydration] Hydrate");

export const hydrateSuccess = createAction(
  "[Hydration] Hydrate Success",
  props<{ state: AppState }>()
);

export const hydrateFailure = createAction("[Hydration] Hydrate Failure");

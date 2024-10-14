import * as fromAuth from '../components/login/store/auth.reducer';
import * as fromUserCars from '../components/car/store/cars.reducer'
import * as fromCarLogbook from "../components/car-logbook/store/car-logbook.reducer";
import * as fromCarClubs from "../components/car-clubs/store/car-club.reducer";
import {ActionReducerMap, MetaReducer} from "@ngrx/store";
import {hydrationMetaReducer} from "./hydration/hydration.reducer";
import {clearStateMetaReducer} from "./hydration/clearState.reducer";


export interface AppState {
  userCars: fromUserCars.State;
  auth: fromAuth.State;
  carLogbook: fromCarLogbook.State;
  carClubs: fromCarClubs.State
}


export const appReducer: ActionReducerMap<AppState> = {
  userCars: fromUserCars.userCarsReducer,
  auth: fromAuth.authReducer,
  carLogbook: fromCarLogbook.carLogbookReducer,
  carClubs: fromCarClubs.carClubReducer
}

export const metaReducers: MetaReducer[] = [
  hydrationMetaReducer,
  clearStateMetaReducer
]

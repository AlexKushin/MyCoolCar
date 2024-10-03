import * as fromAuth from '../components/login/store/auth.reducer';
import * as fromUserCars from '../components/car/store/cars.reducer'
import * as fromCarLogbook from "../components/car-logbook/store/car-logbook.reducer";
import {ActionReducerMap, MetaReducer} from "@ngrx/store";
import {hydrationMetaReducer} from "./hydration/hydration.reducer";


export interface AppState {
  userCars: fromUserCars.State;
  auth: fromAuth.State;
  carLogbook: fromCarLogbook.State
}


export const appReducer: ActionReducerMap<AppState> = {
  userCars: fromUserCars.userCarsReducer,
  auth: fromAuth.authReducer,
  carLogbook: fromCarLogbook.carLogbookReducer
}

export const metaReducers: MetaReducer[] = [
  hydrationMetaReducer
]

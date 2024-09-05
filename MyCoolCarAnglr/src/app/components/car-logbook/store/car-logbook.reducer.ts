
import {CarLogbook} from "../../../models/carLogbook";

import * as LogbookActions from "./car-logbook.actions";


export interface State {
  carLogbook: CarLogbook,
}


const initialState: State = {
  carLogbook: new CarLogbook(-1, [], -1),
}


export function carLogbookReducer(state = initialState, action: LogbookActions.CarLogbookActions) {
  switch (action.type) {

    case LogbookActions.SET_CAR_LOGBOOK:
      return {
        ...state,
        carLogbook: action.payload
      }

    default:
      return state
  }
}

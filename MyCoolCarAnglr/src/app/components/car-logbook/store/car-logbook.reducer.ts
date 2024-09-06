import {CarLogbook} from "../../../models/carLogbook";

import * as LogbookActions from "./car-logbook.actions";
import {SET_NEW_CAR_LOGBOOK_POST} from "./car-logbook.actions";


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
    case LogbookActions.SET_NEW_CAR_LOGBOOK_POST:
      return {
        ...state,
        carLogbook: {
          ...state.carLogbook,  // Spread the existing carLogbook properties
          carLogPosts: [
            ...state.carLogbook.carLogPosts,  // Copy existing carLogPosts
            action.payload  // Append the new CarLogbookPost from action.payload
          ]
        }
      };

    default:
      return state
  }
}

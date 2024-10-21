import {CarClub} from "../../../models/carClub";
import * as CarClubsActions from "./car-club.actions";

export interface State {
  carClubs: CarClub[],
  userCarClubs: CarClub[]
}


const initialState: State = {
  carClubs: [],
  userCarClubs: []
}


export function carClubReducer(state = initialState, action: CarClubsActions.CarClubActions) {
  switch (action.type) {
    case CarClubsActions.SET_CAR_CLUBS:
      return {
        ...state,
        carClubs: [...action.payload]
      }
    case CarClubsActions.SET_USER_CAR_CLUBS:
      return {
        ...state,
        userCarClubs: [...action.payload]
      }
    case CarClubsActions.SET_USER_CAR_CLUB:
      return {
        ...state,
        userCarClubs: [...state.userCarClubs, action.payload]
      }
    default:
      return state
  }
}

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

    // Handle the action to update the specific CarClub in carClubs array
    case CarClubsActions.UPDATE_CAR_CLUB_IN_CAR_CLUBS:
      return {
        ...state,
        carClubs: state.carClubs.map(club =>
          club.id === action.payload.id ? action.payload : club
        ) // Update the specific CarClub in carClubs array
      };
// Handle the action to update the specific CarClub in userCarClubs array
    case CarClubsActions.UPDATE_CAR_CLUB_IN_USER_CAR_CLUBS:
      return {
        ...state,
        userCarClubs: state.userCarClubs.map(club =>
          club.id === action.payload.id ? action.payload : club
        ) // Update the specific CarClub in userCarClubs array
      };
    case CarClubsActions.ADD_CAR_CLUB_TO_USER_CAR_CLUBS:
      return {
        ...state,
        userCarClubs: [...state.userCarClubs, action.payload]  // Add the CarClub to userCarClubs
      };
    case CarClubsActions.REMOVE_CAR_CLUB_FROM_USER_CAR_CLUBS:
      return {
        ...state,
        userCarClubs: state.userCarClubs.filter(club => club.id !== action.payload)  // Remove the CarClub
      };
    default:
      return state
  }
}

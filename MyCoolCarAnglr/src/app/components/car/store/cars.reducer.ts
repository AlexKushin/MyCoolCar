import {Car} from "../../../models/car";
import * as CarsActions from './cars.actions'

export interface State {
  userCars: Car[],
  // userSubscribedCars: Car[]  ???
}


const initialState: State = {
  userCars: [],
  //userSubscribedCars: [] ???
}


export function userCarsReducer(state = initialState, action: CarsActions.UserCarsActions) {
  switch (action.type) {
    case CarsActions.SET_CARS:
      return {
        ...state,
        userCars: [...action.payload]
      }
    case CarsActions.ADD_NEW_USER_CAR:
      return state;
    case CarsActions.SET_USER_CAR:
      return {
        ...state,
        userCars: [...state.userCars, action.payload]
      }
    case CarsActions.UPDATE_CAR:
      const updatedUserCar = {
        ...state.userCars[action.payload.id],
        ...action.payload.userCar
      };

      const updatedUserCars = [...state.userCars];
      updatedUserCars[action.payload.id] = updatedUserCar;
      return {
        ...state, // <- spread operator, to copy all the data from initial array
        userCars: updatedUserCars,
        //we use ... operator to spread elements of payload instead of add array from payload as new element of existing array

      };
    case CarsActions.DELETE_CAR:
      return {
        ...state,
        userCars: state.userCars.filter((userCar) => {
          return userCar.id !== action.payload;
        })
      };
    default:
      return state
  }
}

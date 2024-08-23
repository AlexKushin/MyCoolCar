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
      return state;

    case CarsActions.SET_UPDATED_CAR:
      const updatedCarId = action.payload.id;
      const carIndex = state.userCars.findIndex(car => car.id === updatedCarId);
      // If the car is found, update it
      if (carIndex !== -1) {
        const updatedUserCars = state.userCars.map((car, index) =>
          index === carIndex ? {...car, ...action.payload.userCar} : car
        );

        return {
          ...state,
          userCars: updatedUserCars
        };
      }

      // If the car with the specific id is not found, return the current state
      return state;

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

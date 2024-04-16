import { Action } from "@ngrx/store";
import {Car} from "../../../models/car";

export const SET_CARS = '[Cars] Set Cars';
export const FETCH_CARS = '[Cars] Fetch Cars'
export const ADD_CAR = '[Cars] Add Car'
export const UPDATE_CAR = '[Cars] Update Car'
export const DELETE_CAR = '[Cars] Delete Car'
export const STORE_CARS = '[Cars] Store Car'

export class SetUserCars implements Action {
  readonly type = SET_CARS;

  constructor(public payload: Car[]) { }
}

export class FetchUserCars implements Action {
  readonly type = FETCH_CARS;
}

export class AddUserCar implements Action {
  readonly type = ADD_CAR;

  constructor(public payload: Car) { }
}

export class UpdateUserCar implements Action {
  readonly type = UPDATE_CAR;

  constructor(public payload: { id: number; userCar: Car }) { }
}

export class DeleteUserCar implements Action {
  readonly type = DELETE_CAR;

  constructor(public payload: number) { }
}

export class StoreUserCar implements Action {
  readonly type = STORE_CARS;
}

export type UserCarsActions =
  | SetUserCars
  | FetchUserCars
  | AddUserCar
  | UpdateUserCar
  | DeleteUserCar
  | StoreUserCar;

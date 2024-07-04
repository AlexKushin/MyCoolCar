import { Action } from "@ngrx/store";
import {Car} from "../../../models/car";

export const SET_CARS = '[Cars] Set Cars';
export const SET_USER_CAR = '[Cars] Set New User Car';
export const FETCH_CARS = '[Cars] Fetch Cars'
export const ADD_NEW_USER_CAR = '[Cars] Add New User Car'
export const UPDATE_CAR = '[Cars] Update Car'
export const DELETE_CAR = '[Cars] Delete Car'
export const DELETE_CAR_SUCCESS = '[Cars] Delete Car Success'
export const STORE_CARS = '[Cars] Store Car'
export const USER_CAR_FAIL = '[Cars] User Car Fail'

export class SetUserCars implements Action {
  readonly type = SET_CARS;

  constructor(public payload: Car[]) { }
}

export class FetchUserCars implements Action {
  readonly type = FETCH_CARS;
}

export class AddUserCar implements Action {
  readonly type = ADD_NEW_USER_CAR;

  constructor(public payload: any) { }
}

export class UpdateUserCar implements Action {
  readonly type = UPDATE_CAR;

  constructor(public payload: { id: number; userCar: Car }) { }
}

export class DeleteUserCar implements Action {
  readonly type = DELETE_CAR;

  constructor(public payload: number) { }
}
export class DeleteUserCarSuccess implements Action {
  readonly type = DELETE_CAR_SUCCESS;

  constructor() { }
}

export class StoreUserCar implements Action {
  readonly type = STORE_CARS;
}

export class SetUserCar implements Action {
  readonly type = SET_USER_CAR;

  constructor(public payload: Car) { }
}

export class UserCarFail implements Action {
  readonly type = USER_CAR_FAIL;

  constructor(public payload: string) { }
}

export type UserCarsActions =
  | SetUserCars
  | SetUserCar
  | FetchUserCars
  | AddUserCar
  | UpdateUserCar
  | DeleteUserCar
  | StoreUserCar
  | DeleteUserCarSuccess
  | UserCarFail;

import {Action} from "@ngrx/store";
import {CarClub} from "../../../models/carClub";

export const FETCH_CAR_CLUBS = '[Car Clubs] Fetch Car Clubs'
export const FETCH_USER_CAR_CLUBS = '[Cars] Fetch User Car Clubs'
export const SET_CAR_CLUBS = '[Car Clubs] Set Car Clubs';
export const SET_USER_CAR_CLUBS = '[Car Clubs] Set User Car Clubs';
export const CREATE_CAR_CLUB = '[Car Clubs] Create Car Club';
export const SET_USER_CAR_CLUB = '[Car Clubs] Set User Car Club';


export class FetchCarClubs implements Action {
  readonly type = FETCH_CAR_CLUBS;
}

export class FetchUserCarClubs implements Action {
  readonly type = FETCH_USER_CAR_CLUBS;
}

export class SetCarClubs implements Action {
  readonly type = SET_CAR_CLUBS;

  constructor(public payload: CarClub[]) {
  }
}

export class SetUserCarClubs implements Action {
  readonly type = SET_USER_CAR_CLUBS;

  constructor(public payload: CarClub[]) {
  }
}

export class CreateCarClub implements Action {
  readonly type = CREATE_CAR_CLUB;

  constructor(public payload: any) {
  }
}

export class SetUserCarClub implements Action {
  readonly type = SET_USER_CAR_CLUB;

  constructor(public payload: CarClub) {
  }
}

export type CarClubActions =
  | FetchCarClubs
  | FetchUserCarClubs
  | SetCarClubs
  | SetUserCarClubs
  | CreateCarClub
  | SetUserCarClub

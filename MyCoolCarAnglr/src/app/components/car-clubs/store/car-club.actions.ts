import {Action} from "@ngrx/store";
import {CarClub} from "../../../models/carClub";

export const FETCH_CAR_CLUBS = '[Car Clubs] Fetch Car Clubs'
export const FETCH_USER_CAR_CLUBS = '[Cars] Fetch User Car Clubs'
export const SET_CAR_CLUBS = '[Car Clubs] Set Car Clubs';
export const SET_USER_CAR_CLUBS = '[Car Clubs] Set User Car Clubs';


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


export type CarClubActions =
  | FetchCarClubs
  | FetchUserCarClubs
  | SetCarClubs
  | SetUserCarClubs

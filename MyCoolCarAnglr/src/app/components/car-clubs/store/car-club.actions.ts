import {Action} from "@ngrx/store";
import {CarClub} from "../../../models/carClub";

export const FETCH_CAR_CLUBS = '[Car Clubs] Fetch Car Clubs'
export const FETCH_USER_CAR_CLUBS = '[Car Clubs] Fetch User Car Clubs'
export const SET_CAR_CLUBS = '[Car Clubs] Set Car Clubs';
export const SET_USER_CAR_CLUBS = '[Car Clubs] Set User Car Clubs';
export const CREATE_CAR_CLUB = '[Car Clubs] Create Car Club';
export const SET_USER_CAR_CLUB = '[Car Clubs] Set User Car Club';
export const JOIN_CAR_CLUB = '[Car Clubs] Join Car Club';
export const LEAVE_CAR_CLUB = '[Car Clubs] Leave Car Club';
export const ADD_CAR_CLUB_TO_USER_CAR_CLUBS = '[CarClub] Add Car Club to User Car Clubs';
export const REMOVE_CAR_CLUB_FROM_USER_CAR_CLUBS = '[CarClub] Remove Car Club from User Car Clubs';
export const UPDATE_CAR_CLUB_IN_CAR_CLUBS = '[CarClub] Update Car Club In Car Clubs';

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

export class JoinCarClub implements Action {
  readonly type = JOIN_CAR_CLUB;

  constructor(public payload: number) {
  }
}


export class AddCarClubToUserCarClubs implements Action {
  readonly type = ADD_CAR_CLUB_TO_USER_CAR_CLUBS;

  constructor(public payload: CarClub) {
  }  // The CarClub to be added to userCarClubs
}

export class LeaveCarClub implements Action {
  readonly type = LEAVE_CAR_CLUB;

  constructor(public payload: number) {
  }
}

export class RemoveCarClubFromUserCarClubs implements Action {
  readonly type = REMOVE_CAR_CLUB_FROM_USER_CAR_CLUBS;

  constructor(public payload: number) {
  }  // The carClub ID to be removed from userCarClubs
}

export class UpdateCarClubInCarClubs implements Action {
  readonly type = UPDATE_CAR_CLUB_IN_CAR_CLUBS;

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
  | JoinCarClub
  | AddCarClubToUserCarClubs
  | LeaveCarClub
  | RemoveCarClubFromUserCarClubs
  | UpdateCarClubInCarClubs


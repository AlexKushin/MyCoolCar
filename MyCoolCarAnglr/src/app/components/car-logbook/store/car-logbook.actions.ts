import {Action} from "@ngrx/store";
import {CarLogbook} from "../../../models/carLogbook";
import {CarLogbookPost} from "../../../models/carLogbookPost";

export const FETCH_CAR_LOGBOOK = '[Car-Logbook] Fetch Car-Logbook'
export const SET_CAR_LOGBOOK = '[Car-Logbook] Set Car-Logbook'
export const ADD_NEW_CAR_LOGBOOK_POST = '[Car-Logbook] Add Car-Logbook Post'
export const SET_NEW_CAR_LOGBOOK_POST = '[Car-Logbook] Set Car-Logbook Post'


export class FetchCarLogbook implements Action {
  readonly type = FETCH_CAR_LOGBOOK;

  constructor(public payload: number) {
  }
}

export class SetCarLogbook implements Action {
  readonly type = SET_CAR_LOGBOOK;

  constructor(public payload: CarLogbook) {
  }
}

export class AddCarLogbookPost implements Action {
  readonly type = ADD_NEW_CAR_LOGBOOK_POST;

  constructor(public payload:
                {
                  carLogbookId: number;
                  logbookPost:
                    {
                      topic: string,
                      description: string
                    }
                }
  ) {
  }
}

export class SetCarLogbookPost implements Action {
  readonly type = SET_NEW_CAR_LOGBOOK_POST;

  constructor(public payload: CarLogbookPost) {
  }
}


export type CarLogbookActions =
  | FetchCarLogbook
  | SetCarLogbook
  | AddCarLogbookPost
  | SetCarLogbookPost

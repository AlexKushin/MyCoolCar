import {Action} from "@ngrx/store";
import {CarLogbook} from "../../../models/carLogbook";

export const FETCH_CAR_LOGBOOK = '[Car-Logbook] Fetch Car-Logbook'
export const SET_CAR_LOGBOOK = '[Car-Logbook] Set Car-Logbook'


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


export type CarLogbookActions =
  | FetchCarLogbook
  | SetCarLogbook

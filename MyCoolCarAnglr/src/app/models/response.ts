import {Car} from "./car";

export class Response {

  message: string;
  error: string;


  constructor(message: string, path: string) {
    this.message= message;
    this.error = path;

  }
}

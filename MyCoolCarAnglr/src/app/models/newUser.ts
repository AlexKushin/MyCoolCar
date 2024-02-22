import {Car} from "./car";

export class NewUser {

  constructor(
    public firstName: string,
    public lastName : string,
    public email: string,
    public password: string

  ) {
  }
}

/*export class User {
  firstName!: string;
  lastName!: string;
  email!: string;
  password!: string;
  cars!: Car[]
  constructor(
    _firstName: string,
    _lastName: string,
    _email: string,
    _password: string,
    _cars: Car[]
  ) {
  }


}*/



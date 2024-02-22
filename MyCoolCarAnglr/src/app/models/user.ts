import {Car} from "./car";

export class User {

  id:number;
  ban:boolean;
  registered: string;
  firstName: string;
  lastName:string;
  email: string;
  userCars: Car[]


  constructor(id: number, ban: boolean, registered: string, firstName: string, lastName: string, email: string, userCars: Car[]) {
    this.id = id;
    this.ban = ban;
    this.registered = registered;
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
    this.userCars = userCars;
  }
}
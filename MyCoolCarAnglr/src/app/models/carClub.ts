import {User} from "./user";

export class CarClub {

  constructor(
    public id: number,
    public name: string,
    public description: string,
    public location: string,
    public waitList: User [],
    public members: User [],
    public clubOwnerId: number,
    public accessType: string
  ) {
  }

}

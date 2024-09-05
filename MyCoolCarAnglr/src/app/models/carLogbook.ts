import {CarLogbookPost} from "./carLogbookPost";

export class CarLogbook {

  constructor(
    public id:number,
    public carLogPosts: CarLogbookPost [],
    public carId: number


  ) {
  }

}

export class CarLogbookPost {

  constructor(
    public id: number,
    public topic: string,
    public description: string,
    public createdTime: string,
    public edited: boolean


) {
}

}

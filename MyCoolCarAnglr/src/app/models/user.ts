export class User {

  id: number;
  ban: boolean;
  registered: string;
  firstName: string;
  lastName: string;
  email: string;
  enabled: boolean


  constructor(id: number, ban: boolean, registered: string, firstName: string,
              lastName: string, email: string, enabled: boolean) {
    this.id = id;
    this.ban = ban;
    this.registered = registered;
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
    this.enabled = enabled;
  }
}

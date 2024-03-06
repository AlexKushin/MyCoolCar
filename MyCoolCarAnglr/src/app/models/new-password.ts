export class NewPassword {

  token: string;
  password: string;
  matchingPassword: string;


  constructor(token: string, password: string, matchingPassword: string) {
    this.token = token
    this.password = password;
    this.matchingPassword = matchingPassword;

  }
}

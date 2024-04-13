import {Component, OnInit} from '@angular/core';
import { CommonModule } from '@angular/common';
import {AuthenticationService} from "../../services/authServices/authentication.service";
import { RouterModule } from '@angular/router';
import {Store} from "@ngrx/store";
import * as fromAuth from "../login/store/auth.reducer";
import * as AuthActions from '../login/store/auth.actions'
@Component({
  selector: 'app-menu',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {

  // isUserLoggedIn : boolean = false;

  constructor(
    public authenticationService: AuthenticationService,
    private store: Store<fromAuth.State>
     )
    {}

  ngOnInit(): void {
    //this.isUserLoggedIn = this.authenticationService.isUserLoggedIn();
  }
/*onLogout() {
  this.authenticationService.logout();
}*/
  onLogout() {
    this.store.dispatch(new AuthActions.Logout())
  }
}

import {Component, OnInit} from '@angular/core';
import {CommonModule} from '@angular/common';
import {RouterModule} from '@angular/router';
import {Store} from "@ngrx/store";
import {map, Subscription} from "rxjs";
import {User} from "../../models/user";
import * as AuthActions from '../login/store/auth.actions'
import * as fromApp from '../../store/app.reducer';

@Component({
  selector: 'app-menu',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {

  isUserAuthenticated = false;
  private userSub: Subscription;

  constructor(
    private store: Store<fromApp.AppState>
  ) {
  }

  ngOnInit() {
    console.log('HeaderComponent OnInit:')
    console.log('isAuthenticated before')
    console.log(this.isUserAuthenticated)
    this.userSub = this.store
      .select('auth')
      .pipe(map(userState => userState.user))
      .subscribe((user: User) => {
        this.isUserAuthenticated = !!user;
        console.log("user: ")
        console.log(user);
        console.log('isAuthenticated after')
        console.log(this.isUserAuthenticated)
      });
  }

  onLogout() {
    this.store.dispatch(new AuthActions.Logout())
  }
}

import {Component, OnDestroy, OnInit} from '@angular/core';
import {CommonModule} from '@angular/common';
import {ActivatedRoute, Router} from "@angular/router";
import {MatCardModule} from "@angular/material/card";
import {CarCardComponent} from "../car/car-card/car-card.component";
import {Store} from "@ngrx/store";
import * as fromAuth from "../login/store/auth.reducer";
import {User} from "../../models/user";
import {map, Subscription} from "rxjs";

@Component({
  selector: 'app-welcome',
  standalone: true,
  imports: [CommonModule, MatCardModule],
  templateUrl: './welcome.component.html',
  styleUrls: ['./welcome.component.css']
})
export class WelcomeComponent implements OnInit, OnDestroy {

  public user: User | null
  subscription: Subscription;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private store: Store<{ auth: fromAuth.State }>
  ) {
  }

  ngOnInit(): void {
    this.subscription = this.store.select('auth')
      .pipe(map(userState => userState.user))
      .subscribe((user: User | null) => this.user = user)
    console.log("user= " + this.user)
  }

  ngOnDestroy(): void {
    this.subscription.unsubscribe();
  }


}

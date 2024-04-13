import {Component, OnInit} from '@angular/core';
import { CommonModule } from '@angular/common';
import {ActivatedRoute, Router} from "@angular/router";
import {UserService} from "../../services/user.service";
import {MatCardModule} from "@angular/material/card";
import {CarCardComponent} from "../car/car-card/car-card.component";
import {Store} from "@ngrx/store";
import * as fromAuth from "../login/store/auth.reducer";
import {User} from "../../models/user";
import {map, Subscription} from "rxjs";

@Component({
  selector: 'app-welcome',
  standalone: true,
  imports: [CommonModule, MatCardModule, CarCardComponent],
  templateUrl: './welcome.component.html',
  styleUrls: ['./welcome.component.css']
})
export class WelcomeComponent implements OnInit {



  name = ''
  user!: User
  subscription!: Subscription;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private userService: UserService,
    private store: Store<{ auth:fromAuth.State }>
  ) {
  }

/*
  ngOnInit(): void {
    console.log(this.route.snapshot.params["name"])
    this.name = this.route.snapshot.params["name"]
    this.getUser()
  }
*/

  ngOnInit(): void {
    this.subscription = this.store.select('auth')
      .pipe(map(userState => userState.user))
      .subscribe((user: User) => this.user = user)
    console.log("user= " + this.user)
  }
  addCar(){
    this.router.navigate(['cars'])
  }
  getUser(){
    this.userService.getCurrentUser().subscribe(
      response => {
      console.log(response)
      this.user = response;
    }
    )
  }
}

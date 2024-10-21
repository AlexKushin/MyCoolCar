import {Component, OnDestroy, OnInit} from '@angular/core';
import {CarsListComponent} from "../car/cars-garage/cars-list/cars-list.component";
import {NgIf} from "@angular/common";
import {map, Subscription} from "rxjs";
import {Router} from "@angular/router";
import {Store} from "@ngrx/store";
import * as fromApp from "../../store/app.reducer";
import {CarClub} from "../../models/carClub";
import {CarClubsListComponent} from "./car-clubs-list/car-clubs-list.component";

@Component({
  selector: 'app-car-clubs',
  standalone: true,
  imports: [
    CarsListComponent,
    NgIf,
    CarClubsListComponent
  ],
  templateUrl: './car-clubs.component.html',
  styleUrl: './car-clubs.component.css'
})
export class CarClubsComponent implements OnInit, OnDestroy {
  isMyCars = true;

  public userCarClubs: CarClub[];
  public carClubs: CarClub[];
  subscription: Subscription;
  subscription1: Subscription;

  constructor(
    private router: Router,
    private store: Store<fromApp.AppState>
  ) {
  }

  ngOnInit(): void {
    this.subscription = this.store.select('carClubs')
      .pipe(map(carClubsState => carClubsState.carClubs))
      .subscribe((carClubs: CarClub[]) => {
        this.carClubs = carClubs
      })

    this.subscription1 = this.store.select('carClubs')
      .pipe(map(carClubsState => carClubsState.userCarClubs))
      .subscribe((userCarClubs: CarClub[]) => {
        this.userCarClubs = userCarClubs
      })

  }

  ngOnDestroy(): void {
    this.subscription.unsubscribe();
    this.subscription1.unsubscribe();
  }

  createNewCarClub() {
    this.router.navigate(['car_clubs/new'])
  }

  showMyCarsClubs() {
    this.isMyCars = true
  }

  showClubs() {
    this.isMyCars = false
  }
}

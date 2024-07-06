import {Component, OnDestroy, OnInit} from '@angular/core';
import {CarsListComponent} from "./cars-list/cars-list.component";
import {NewCarComponent} from "./new-car/new-car.component";
import {ActivatedRoute, Router, RouterLink} from "@angular/router";
import {Store} from "@ngrx/store";
import * as fromUserCars from "../store/cars.reducer";
import {NgIf} from "@angular/common";
import {map, Subscription} from "rxjs";
import {Car} from "../../../models/car";

@Component({
  selector: 'app-cars-garage',
  standalone: true,
  imports: [
    CarsListComponent,
    NewCarComponent,
    NgIf,
    RouterLink
  ],
  templateUrl: './cars-garage.component.html',
  styleUrl: './cars-garage.component.css'
})
export class CarsGarageComponent implements OnInit, OnDestroy {
  isMyCars = true;

  public userCars: Car[];
  public userSubscribedCars: Car[];
  subscription: Subscription;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private store: Store<{ userCarsState: fromUserCars.State }>
  ) {
  }

  ngOnInit(): void {
    this.subscription = this.store.select('userCarsState')
      .pipe(map(userCarsState => userCarsState.userCars))
      .subscribe((userCars: Car[]) => this.userCars = userCars)
  }

  ngOnDestroy(): void {
    this.subscription.unsubscribe();
  }

  addCar() {
    this.router.navigate(['cars/new'])
  }

  showMyCars() {
    this.isMyCars = true
  }

  showSubscribedCars() {
    this.isMyCars = false
  }
}

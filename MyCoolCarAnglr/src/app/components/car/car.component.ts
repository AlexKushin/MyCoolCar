import {Component, OnDestroy, OnInit} from '@angular/core';
import {Car} from "../../models/car";
import {ActivatedRoute, Router} from "@angular/router";
import {Store} from "@ngrx/store";
import * as fromUserCars from "./store/cars.reducer";
import * as UserCarsActions from "./store/cars.actions"
import {map, Subscription} from "rxjs";
import {switchMap} from "rxjs/operators";
import {NgForOf, NgIf} from "@angular/common";
import {CarLogbookComponent} from "../car-logbook/car-logbook.component";
import {CarsListComponent} from "./cars-garage/cars-list/cars-list.component";

@Component({
  selector: 'app-car',
  standalone: true,
  templateUrl: './car.component.html',
  imports: [
    NgForOf,
    NgIf,
    CarLogbookComponent,
    CarsListComponent
  ],
  styleUrls: ['./car.component.css']
})
export class CarComponent implements OnInit, OnDestroy {


  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private store: Store<{ userCarsState: fromUserCars.State }>
  ) {
  }

  car: Car;
  id: number;
  subscription: Subscription

  ngOnInit(): void {
    this.subscription = this.route.params.pipe(map(params => {
        return +params['id'];
      }),
      switchMap(id => {
        this.id = id;
        return this.store.select('userCarsState')
      }),
      map(userCarsState => {
        return userCarsState.userCars.find((car) => {
          return car.id === this.id;
        });
      })
    )
      .subscribe(car => {
        // @ts-ignore
        this.car = car;
      })

  }

  ngOnDestroy() {
    this.subscription.unsubscribe();
  }


  editCar() {
    this.router.navigate(['cars/edit'], {state: {data: this.car}})
  }

  addNewLog() {

  }

  deleteCar() {
    this.store.dispatch(new UserCarsActions.DeleteUserCar(this.id))
  }
}

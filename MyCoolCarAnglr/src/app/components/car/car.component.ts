import {Component, OnDestroy, OnInit} from '@angular/core';
import {Car} from "../../models/car";
import {ActivatedRoute, Router} from "@angular/router";
import {Store} from "@ngrx/store";
import {map, Subscription} from "rxjs";
import {switchMap} from "rxjs/operators";
import {NgForOf, NgIf} from "@angular/common";
import {CarLogbookComponent} from "../car-logbook/car-logbook.component";
import * as UserCarsActions from "./store/cars.actions"
import * as fromApp from '../../store/app.reducer';

@Component({
  selector: 'app-car',
  standalone: true,
  templateUrl: './car.component.html',
  imports: [
    NgForOf,
    NgIf,
    CarLogbookComponent,
  ],
  styleUrls: ['./car.component.css']
})
export class CarComponent implements OnInit, OnDestroy {


  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private store: Store<fromApp.AppState>
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
        //return this.store.select('userCarsState')
        return this.store.select('userCars')
      }),
      map(userCarsState => {
        return userCarsState.userCars.find((car) => {
          return car.id === this.id;
        });
      })
    )
      .subscribe(car => {
        this.car = car;
      })

  }

  ngOnDestroy() {
    this.subscription.unsubscribe();
  }


  editCar() {
    this.router.navigate(['edit'], {relativeTo: this.route, state: {data: this.car}})
  }


  deleteCar() {
    this.store.dispatch(new UserCarsActions.DeleteUserCar(this.id))
  }
}

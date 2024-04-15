import { Component } from '@angular/core';
import {CarsListComponent} from "./cars-list/cars-list.component";
import {NewCarComponent} from "./new-car/new-car.component";
import {ActivatedRoute, Router} from "@angular/router";
import {UserService} from "../../../services/user.service";
import {Store} from "@ngrx/store";
import * as fromAuth from "../../login/store/auth.reducer";

@Component({
  selector: 'app-cars-garage',
  standalone: true,
  imports: [
    CarsListComponent,
    NewCarComponent
  ],
  templateUrl: './cars-garage.component.html',
  styleUrl: './cars-garage.component.css'
})
export class CarsGarageComponent {

  constructor(
    private route: ActivatedRoute,
    private router: Router,

    private store: Store<{ auth: fromAuth.State }>
  ) {
  }
  addCar() {
    this.router.navigate(['cars/new'])
  }

}

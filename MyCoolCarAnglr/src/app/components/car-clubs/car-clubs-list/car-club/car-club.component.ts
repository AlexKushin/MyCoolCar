import {Component, OnDestroy, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {Store} from "@ngrx/store";
import * as fromApp from "../../../../store/app.reducer";
import * as CarClubActions from "../../store/car-club.actions"
import {Car} from "../../../../models/car";
import {map, Subscription} from "rxjs";
import {switchMap} from "rxjs/operators";
import {CarClub} from "../../../../models/carClub";

@Component({
  selector: 'app-car-club',
  standalone: true,
  imports: [],
  templateUrl: './car-club.component.html',
  styleUrl: './car-club.component.css'
})
export class CarClubComponent implements OnInit, OnDestroy {


  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private store: Store<fromApp.AppState>
  ) {
  }

  carClub: CarClub;
  id: number;
  subscription: Subscription

  ngOnInit(): void {
    this.subscription = this.route.params.pipe(
      map(params => +params['id']), // Get the id from the route params
      switchMap(id => {
        this.id = id;
        return this.store.select('carClubs'); // Select the carClubs state from the store
      }),
      map(carClubsState => {
        // First, check if the carClub is in userCarClubs
        let carClub = carClubsState.userCarClubs.find((carClub) => carClub.id === this.id);

        // If it's not in userCarClubs, fallback to checking the main carClubs array
        if (!carClub) {
          carClub = carClubsState.carClubs.find((carClub) => carClub.id === this.id);
        }

        return carClub; // Return the found carClub or undefined
      })
    )
      .subscribe(carClub => {
        this.carClub = carClub; // Assign the found carClub (or undefined if not found)
      });
  }

  ngOnDestroy() {
    this.subscription.unsubscribe();
  }


  editCarClub() {
    //edit logic
  }


  deleteCarClub() {
   //delete logic
  }
}

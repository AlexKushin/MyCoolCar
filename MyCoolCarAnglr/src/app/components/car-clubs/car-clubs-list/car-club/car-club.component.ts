import {Component, OnDestroy, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {Store} from "@ngrx/store";
import {map, Observable, of, Subscription} from "rxjs";
import {switchMap} from "rxjs/operators";
import {CarClub} from "../../../../models/carClub";
import {AsyncPipe, NgForOf, NgIf, NgOptimizedImage} from "@angular/common";
import * as fromApp from "../../../../store/app.reducer";
import {CarClubCardComponent} from "../car-club-card/car-club-card.component";
import {CarClubAdminPageComponent} from "./car-club-admin-page/car-club-admin-page.component";

@Component({
  selector: 'app-car-club',
  standalone: true,
  imports: [
    NgForOf,
    NgOptimizedImage,
    NgIf,
    AsyncPipe,
    CarClubCardComponent,
    CarClubAdminPageComponent
  ],
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
  carClubSubscription: Subscription
  isUserOwner$: Observable<boolean>;

  ngOnInit(): void {
    // Observable for checking if user is the car club owner
    this.isUserOwner$ = this.store.select('auth').pipe(
      map(authState => authState.user && this.carClub ? this.carClub.clubOwnerId === authState.user.id : false)
    );
    this.carClubSubscription = this.route.params.pipe(
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
    this.carClubSubscription.unsubscribe();
  }


  onManageCarClub() {
    this.router.navigate(['admin_page'], {relativeTo: this.route/*,state: {data: this.carClub}*/})
  }
}

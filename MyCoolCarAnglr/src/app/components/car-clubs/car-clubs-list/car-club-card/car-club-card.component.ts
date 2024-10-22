import {Component, Input, OnDestroy, OnInit} from '@angular/core';
import {RouterLink} from "@angular/router";
import {CarClub} from "../../../../models/carClub";
import {AsyncPipe, NgIf} from "@angular/common";
import {Store} from "@ngrx/store";
import * as fromApp from "../../../../store/app.reducer";
import * as CarClubsActions from "../../store/car-club.actions"
import {combineLatest, map, Observable, of, Subscription} from "rxjs";

@Component({
  selector: 'app-car-club',
  standalone: true,
  imports: [
    RouterLink, NgIf, AsyncPipe
  ],
  templateUrl: './car-club-card.component.html',
  styleUrl: './car-club-card.component.css'
})
export class CarClubCardComponent implements OnInit, OnDestroy {
  @Input() carClub: CarClub;
  subscription: Subscription
  carClubExistsInUserCarClubs$: Observable<boolean>;
  isUserOwner: boolean = false
  showLeaveButton$: Observable<boolean>;


  constructor(
    private store: Store<fromApp.AppState>
  ) {
  }

  ngOnInit(): void {
    this.carClubExistsInUserCarClubs$ = this.store.select('carClubs').pipe(
      map(carClubsState => {
        // Check if the CarClub exists in userCarClubs array
        // Return true if found in array, otherwise false
        return carClubsState.userCarClubs.some(carClub => carClub.id === this.carClub.id);
      })
    );
    this.subscription = this.store.select('auth').pipe(
      map(authState => {
        this.isUserOwner = this.carClub.clubOwnerId === authState.user.id
      })
    ).subscribe()
    // Combine the two observables into one
    this.showLeaveButton$ = combineLatest([this.carClubExistsInUserCarClubs$, this.isUserOwner$()]).pipe(
      map(([exists, isOwner]) => exists && !isOwner)
    );

  }

  // Helper function to expose isUserOwner as an observable
  private isUserOwner$(): Observable<boolean> {
    return of(this.isUserOwner);
  }

  onJoinCarClub() {
    this.store.dispatch(new CarClubsActions.JoinCarClub(this.carClub.id))
  }

  onLeaveCarClub() {
    this.store.dispatch(new CarClubsActions.LeaveCarClub(this.carClub.id))
  }

  ngOnDestroy(): void {
    // Unsubscribe to avoid memory leaks
    if (this.subscription) {
      this.subscription.unsubscribe();
    }
  }

}

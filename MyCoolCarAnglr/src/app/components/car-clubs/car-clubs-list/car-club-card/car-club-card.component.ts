import {Component, Input, OnInit} from '@angular/core';
import {RouterLink} from "@angular/router";
import {CarClub} from "../../../../models/carClub";
import {AsyncPipe, NgIf} from "@angular/common";
import {Store} from "@ngrx/store";
import * as fromApp from "../../../../store/app.reducer";
import {map, Observable, Subscription} from "rxjs";

@Component({
  selector: 'app-car-club',
  standalone: true,
  imports: [
    RouterLink, NgIf, AsyncPipe
  ],
  templateUrl: './car-club-card.component.html',
  styleUrl: './car-club-card.component.css'
})
export class CarClubCardComponent implements OnInit {
  @Input() carClub: CarClub;
  subscription: Subscription
  carClubExistsInUserCarClubs$: Observable<boolean>;


  constructor(
    private store: Store<fromApp.AppState>
  ) {
  }

  ngOnInit(): void {
    this.carClubExistsInUserCarClubs$  = this.store.select('carClubs').pipe(
      map(carClubsState => {
        // Check if the CarClub exists in userCarClubs array
        // Return true if found in array, otherwise false
        return  carClubsState.userCarClubs.some(carClub => carClub.id === this.carClub.id);
      })
    );
  }

}

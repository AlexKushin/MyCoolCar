import {Component, Input, OnInit} from '@angular/core';
import {CarClub} from "../../../../../models/carClub";
import {AsyncPipe, NgForOf, NgIf} from "@angular/common";
import * as CarClubsActions from "../../../store/car-club.actions";
import {Store} from "@ngrx/store";
import * as fromApp from "../../../../../store/app.reducer";
import {map, Observable, Subscription} from "rxjs";
import {switchMap} from "rxjs/operators";
import {ActivatedRoute, Router} from "@angular/router";

@Component({
  selector: 'app-car-club-admin-page',
  standalone: true,
  imports: [
    NgForOf,
    AsyncPipe,
    NgIf
  ],
  templateUrl: './car-club-admin-page.component.html',
  styleUrl: './car-club-admin-page.component.css'
})
export class CarClubAdminPageComponent implements OnInit {
  carClubSubscription: Subscription
  id: number;
  carClub: CarClub;

  constructor(
    private store: Store<fromApp.AppState>,
    private route: ActivatedRoute,
  ) {
  }


  ngOnInit() {
    //this.carClub = history.state.data;
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


  onConfirmMember(waitUserId: number) {
    this.store.dispatch(new CarClubsActions.ConfirmCarClubMember({
      carClubId: this.carClub.id,
      waitUserId: waitUserId
    }))
  }

  onRefuseMember(waitUserId: number) {
    this.store.dispatch(new CarClubsActions.RefuseCarClubMember({
      carClubId: this.carClub.id,
      waitUserId: waitUserId
    }))
  }

  editCarClub() {
    //edit logic
  }


  deleteCarClub() {
    //delete logic
  }

  onDeleteMember(id: number) {
    //todo logic for deleting member from Car Club

  }

  onDeleteCarClub() {
    //todo logic for deleting Car Club
  }
}

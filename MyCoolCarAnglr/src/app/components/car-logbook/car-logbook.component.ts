import {Component, OnDestroy, OnInit} from '@angular/core';
import {CarLogbookPostComponent} from "./car-logbook-post/car-logbook-post.component";
import {CarLogbook} from "../../models/carLogbook";
import {CarCardComponent} from "../car/car-card/car-card.component";
import {NgForOf} from "@angular/common";
import {ActivatedRoute, Router, RouterLink, RouterLinkActive, RouterModule} from "@angular/router";
import {Store} from "@ngrx/store";

import * as fromCarLogbook from "../car-logbook/store/car-logbook.reducer";
import {map, Subscription} from "rxjs";


@Component({
  selector: 'app-car-logbook',
  standalone: true,
  imports: [
    CarLogbookPostComponent,
    CarCardComponent,
    NgForOf,
    RouterLink,
    RouterModule,
    RouterLinkActive
  ],
  templateUrl: './car-logbook.component.html',
  styleUrl: './car-logbook.component.css'
})
export class CarLogbookComponent implements OnInit, OnDestroy {
  carLogbook: CarLogbook
  subscription: Subscription;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private store: Store<{ carLogbookState: fromCarLogbook.State }>
  ) {
  }

  ngOnInit(): void {
    this.subscription = this.store.select('carLogbookState')
      .pipe(map(carLogbookState => carLogbookState.carLogbook))
      .subscribe((carLogbook: CarLogbook) => this.carLogbook = carLogbook)

  }
  ngOnDestroy(): void {
    this.subscription.unsubscribe();
  }

  addNewLogPost() {
    this.router.navigate([`car-logbook/${this.carLogbook.id}/car-logbook-posts/new`], { relativeTo: this.route });
  }
}
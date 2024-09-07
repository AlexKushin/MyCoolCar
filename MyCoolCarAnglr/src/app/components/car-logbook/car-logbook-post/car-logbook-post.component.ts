import {Component, OnDestroy, OnInit} from '@angular/core';
import {CarLogbookPost} from "../../../models/carLogbookPost";
import {ActivatedRoute} from "@angular/router";
import {Store} from "@ngrx/store";
import * as fromCarLogbook from "../store/car-logbook.reducer";
import * as CarLogbookActions from "../store/car-logbook.actions"
import {map, Subscription} from "rxjs";
import {switchMap} from "rxjs/operators";


@Component({
  selector: 'app-car-logbook-post',
  standalone: true,
  imports: [],
  templateUrl: './car-logbook-post.component.html',
  styleUrl: './car-logbook-post.component.css'
})
export class CarLogbookPostComponent implements OnInit, OnDestroy {

  constructor(
    private route: ActivatedRoute,
    private store: Store<{ carLogbookState: fromCarLogbook.State }>
  ) {
  }

  logbookPost: CarLogbookPost
  logbookPostId: number;
  subscription: Subscription

  ngOnInit(): void {
    console.log("log post:")
    this.subscription = this.route.params.pipe(map(params => {
        return +params['car-logbook-postId'];
      }),
      switchMap(id => {
        this.logbookPostId = id;
        return this.store.select('carLogbookState')
      }),
      map(carLogbookState => {
        return carLogbookState.carLogbook.carLogPosts.find((logbookPost) => {
          return logbookPost.id === this.logbookPostId;
        });
      })
    )
      .subscribe(logbookPost => {
        this.logbookPost = logbookPost;
      })


  }

  ngOnDestroy() {
    this.subscription.unsubscribe();
  }

  editLogbookPost() {

  }

  deleteLogbookPost() {
    this.store.dispatch(new CarLogbookActions.DeleteCarLogbookPost(this.logbookPostId))
  }
}

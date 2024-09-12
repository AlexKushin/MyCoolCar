import {Component, Injector, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {CommonModule} from '@angular/common';
import {RouterLink} from "@angular/router";
import {FormsModule, NgForm} from '@angular/forms';
import {Car} from "../../models/car";
import {CarService} from "../../services/car.service";
import {CarCardComponent} from "../car/car-card/car-card.component";
import {CarCardSliderComponent} from "../car/car-card-slider/car-card-slider.component";
import * as fromAuth from './store/auth.reducer'
import {Store} from "@ngrx/store";
import * as AuthActions from './store/auth.actions'
import {PlaceholderDirective} from "../shared/placeholder/placeholder.directive";
import {Subscription} from "rxjs";
import {WarningComponent} from "../shared/warning/warning.component";

@Component({
  selector: 'app-login',
  standalone: true,
  imports:
    [
      CommonModule,
      FormsModule,
      CarCardComponent,
      CarCardSliderComponent,
      RouterLink,
      PlaceholderDirective
    ],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit, OnDestroy {

  cars: Car[] = []

  error: string = null;
  @ViewChild(PlaceholderDirective, {static: false}) alertHost: PlaceholderDirective;

  private closeSub: Subscription;
  private storeSub: Subscription;

  constructor(
    private carService: CarService,
    private store: Store<{ auth: fromAuth.State }>,
    private injector: Injector
  ) {
  }

  ngOnInit(): void {
    // this.refreshTopCars()
    this.storeSub = this.store.select('auth').subscribe(authState => {
      this.error = authState.authError;
      if (this.error) {
        this.showErrorAlert(this.error);
      }
    });

  }


  refreshTopCars() {
    this.carService.getTopCars().subscribe(
      response => {
        console.log(response)
        this.cars = response;
      }
    )
  }

  onSubmit(authForm: NgForm) {
    if (!authForm.valid) {
      return;
    }
    const email = authForm.value.email;
    const password = authForm.value.password;

    this.store.dispatch(new AuthActions.LoginStart(
      {email: email, password: password}))
    authForm.reset();
  }

  ngOnDestroy() {
    if (this.closeSub) {
      this.closeSub.unsubscribe();
    }
    if (this.storeSub) {
      this.storeSub.unsubscribe();
    }
  }


  private showErrorAlert(message: string) {
    const hostViewContainerRef = this.alertHost.viewContainerRef;
    hostViewContainerRef.clear();

    const componentRef = hostViewContainerRef.createComponent(WarningComponent, {
      injector: this.injector
    });

    componentRef.instance.message = message;
    this.closeSub = componentRef.instance.close.subscribe(() => {
      this.closeSub.unsubscribe();
      hostViewContainerRef.clear();
    });
  }

}

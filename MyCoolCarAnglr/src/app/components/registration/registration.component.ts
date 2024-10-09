import {Component, Injector, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {CommonModule} from '@angular/common';
import {FormsModule, NgForm, ReactiveFormsModule} from "@angular/forms";
import {Router} from "@angular/router";
import {Store} from "@ngrx/store";
import {PlaceholderDirective} from "../shared/placeholder/placeholder.directive";
import {Subscription} from "rxjs";
import {WarningComponent} from "../shared/warning/warning.component";
import * as AuthActions from '../login/store/auth.actions'
import * as fromApp from '../../store/app.reducer';

@Component({
  selector: 'app-registration',
  standalone: true,
  imports: [CommonModule, FormsModule, ReactiveFormsModule, PlaceholderDirective],
  templateUrl: './registration.component.html',
  styleUrls: ['./registration.component.css']
})
export class RegistrationComponent implements OnInit, OnDestroy {
  error: string = null;
  @ViewChild(PlaceholderDirective, {static: false}) alertHost: PlaceholderDirective;

  private closeSub: Subscription;
  private storeSub: Subscription;

  constructor(
    private store: Store<fromApp.AppState>,
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

  registerNewUser(registrationForm: NgForm) {
    if (!registrationForm.valid) {
      return;
    }
    this.store.dispatch(new AuthActions.RegistrationStart(registrationForm.value));
    registrationForm.reset();
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

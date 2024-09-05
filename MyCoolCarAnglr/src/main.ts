import {bootstrapApplication, BrowserModule} from "@angular/platform-browser";
import {AppComponent} from "./app/app.component";
import {provideState, provideStore} from "@ngrx/store";
import {AppRoutingModule} from "./app/app-routing.module";
import {importProvidersFrom} from "@angular/core";
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {HTTP_INTERCEPTORS, HttpClientModule} from "@angular/common/http";
import {ToastrModule, ToastrService} from "ngx-toastr";
import {HttpInterceptorAuthService} from "./app/services/authServices/http-interceptor-auth.service";
import {RouterModule} from "@angular/router";
import {provideEffects} from '@ngrx/effects';
import {authReducer} from "./app/components/login/store/auth.reducer";
import {AuthEffects} from "./app/components/login/store/auth.effects";
import {StoreDevtoolsModule} from "@ngrx/store-devtools";
import {environment} from "./environments/environment";
import {userCarsReducer} from "./app/components/car/store/cars.reducer";
import {UserCarsEffects} from "./app/components/car/store/cars.effects";
import {carLogbookReducer} from "./app/components/car-logbook/store/car-logbook.reducer";
import {CarLogbookEffects} from "./app/components/car-logbook/store/car-logbook.effects";


bootstrapApplication(AppComponent, {
    providers: [
      provideStore(authReducer),
      provideStore(userCarsReducer),
      provideStore(carLogbookReducer),
      provideState({name: 'auth', reducer: authReducer}),
      provideState({name: 'userCarsState', reducer: userCarsReducer}),
      provideState({name: 'carLogbookState', reducer: carLogbookReducer}),
      importProvidersFrom(
        BrowserModule,
        ReactiveFormsModule,
        BrowserAnimationsModule,
        BrowserModule,
        AppRoutingModule,
        FormsModule,
        HttpClientModule,
        StoreDevtoolsModule.instrument({logOnly: environment.production}),
        RouterModule.forRoot([]),
        ToastrModule.forRoot(
          {
            positionClass: 'toast-bottom-right'
          })
      ),
      {provide: HTTP_INTERCEPTORS, useClass: HttpInterceptorAuthService, multi: true},
      {provide: ToastrService, useClass: ToastrService},
      provideEffects([AuthEffects,UserCarsEffects, CarLogbookEffects]),
    ],
  }
)

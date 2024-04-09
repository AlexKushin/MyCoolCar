import {bootstrapApplication, BrowserModule} from "@angular/platform-browser";
import {AppComponent} from "./app/app.component";
import {provideStore} from "@ngrx/store";
import {AppRoutingModule} from "./app/app-routing.module";
import {importProvidersFrom} from "@angular/core";
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {HTTP_INTERCEPTORS, HttpClientModule} from "@angular/common/http";
import {ToastrModule, ToastrService} from "ngx-toastr";
import {HttpInterceptorAuthService} from "./app/services/authServices/http-interceptor-auth.service";
import {RouterModule} from "@angular/router";

bootstrapApplication(AppComponent, {
    providers: [provideStore(), importProvidersFrom(
      BrowserModule,
      ReactiveFormsModule,
      BrowserAnimationsModule,
      BrowserModule,
      AppRoutingModule,
      FormsModule,
      HttpClientModule,
      RouterModule.forRoot([]),
      ToastrModule.forRoot({
        positionClass: 'toast-bottom-right'
      })
    ),
      {provide: HTTP_INTERCEPTORS, useClass: HttpInterceptorAuthService, multi: true},
      {provide: ToastrService, useClass: ToastrService}
    ]
  }
)

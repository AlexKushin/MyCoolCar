import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import { AppRoutingModule } from './app-routing.module';

import { AppComponent } from './app.component';
import { HeaderComponent } from './components/header/header.component';
import { FooterComponent } from './components/footer/footer.component';
import {HTTP_INTERCEPTORS, HttpClientModule} from '@angular/common/http';
import { RouterModule } from '@angular/router';
import {HttpInterceptorAuthService} from "./services/authServices/http-interceptor-auth.service";


import {ToastrModule, ToastrService} from 'ngx-toastr';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { CarComponent } from './components/car/car.component';
@NgModule({
  declarations: [
    AppComponent,
    CarComponent
  ],
  imports: [
    ReactiveFormsModule,
    BrowserAnimationsModule,
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    HeaderComponent,
    FooterComponent,
    HttpClientModule,
    RouterModule.forRoot([]),
    ToastrModule.forRoot({
      positionClass :'toast-bottom-right'
    })

  ],
  providers: [
    {provide: HTTP_INTERCEPTORS, useClass: HttpInterceptorAuthService, multi: true},
    {provide: ToastrService, useClass: ToastrService}
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }

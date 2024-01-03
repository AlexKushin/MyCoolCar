import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';
import { AppRoutingModule } from './app-routing.module';

import { AppComponent } from './app.component';
import { MenuComponent } from './components/menu/menu.component';
import { FooterComponent } from './components/footer/footer.component';
import {HTTP_INTERCEPTORS, HttpClientModule} from '@angular/common/http';
import { RouterModule } from '@angular/router';
import {HttpInterceptorAuthService} from "./services/authServices/http-interceptor-auth.service";

@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    MenuComponent,
    FooterComponent,
    HttpClientModule,
    RouterModule.forRoot([])

  ],
  providers: [
    {provide: HTTP_INTERCEPTORS, useClass: HttpInterceptorAuthService, multi: true}
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }

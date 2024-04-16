import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {LoginComponent} from "./components/login/login.component";
import {ErrorComponent} from "./components/error/error.component";
import {WelcomeComponent} from "./components/welcome/welcome.component";
import {authGuard} from "./services/authServices/auth.guard";
import {NewCarComponent} from "./components/car/cars-garage/new-car/new-car.component";
import {RegistrationComponent} from "./components/registration/registration.component";
import {
  ConfirmRegistrationComponent
} from "./components/registration/confirm-registration/confirm-registration.component";
import {PasswordResetComponent} from "./components/login/password-reset/password-reset.component";
import {PasswordChangeComponent} from "./components/login/password-change/password-change.component";
import {CarsGarageComponent} from "./components/car/cars-garage/cars-garage.component";
import {UserResolverService} from "./components/login/user-resolve.service";
import {UserCarsResolverService} from "./components/car/user-cars-resolve.service";



const routes: Routes = [
  {path:"", component: LoginComponent},
  {path:"login", component: LoginComponent},
  {path:"password/reset", component: PasswordResetComponent},
  {path:"password/change", component: PasswordChangeComponent},
  {path:"registration", component: RegistrationComponent},
  {path:"registration/confirm", component: ConfirmRegistrationComponent},
  {path:"welcome", component: WelcomeComponent, resolve: [UserResolverService],canActivate: [authGuard]},
  {path:"cars", component: CarsGarageComponent,resolve: [UserCarsResolverService], canActivate: [authGuard]},
  {path:"cars/new", component: NewCarComponent, canActivate: [authGuard]},
  {path:"**", component: ErrorComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }

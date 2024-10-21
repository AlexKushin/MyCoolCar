import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
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
import {CarComponent} from "./components/car/car.component";
import {EditCarComponent} from "./components/car/cars-garage/edit-car/edit-car.component";
import {CarLogbookResolverService} from "./components/car-logbook/car-logbook.resolve.service";
import {NewCarLogbookPostComponent} from "./components/car-logbook/new-car-logbook-post/new-car-logbook-post.component";
import {CarLogbookPostComponent} from "./components/car-logbook/car-logbook-post/car-logbook-post.component";
import {
  EditCarLogbookPostComponent
} from "./components/car-logbook/edit-car-logbook-post/edit-car-logbook-post.component";
import {CarClubsComponent} from "./components/car-clubs/car-clubs.component";
import {CarClubsResolverService} from "./components/car-clubs/car-clubs-resolve.service";
import {UserCarClubsResolverService} from "./components/car-clubs/user-car-clubs-resolve.service";
import {NewCarClubComponent} from "./components/car-clubs/new-car-club/new-car-club.component";
import {CarClubComponent} from "./components/car-clubs/car-clubs-list/car-club/car-club.component";


const routes: Routes = [
  {path: "", component: WelcomeComponent},
  {path: "welcome", component: WelcomeComponent},
  {path: "login", component: LoginComponent},
  {path: "password/reset", component: PasswordResetComponent},
  {path: "password/change", component: PasswordChangeComponent},
  {path: "registration", component: RegistrationComponent},
  {path: "registration/confirm", component: ConfirmRegistrationComponent},
  {path: "current/welcome", component: WelcomeComponent, resolve: [UserResolverService], canActivate: [authGuard]},
  {path: "cars", component: CarsGarageComponent, resolve: [UserCarsResolverService], canActivate: [authGuard]},
  {path: "cars/new", component: NewCarComponent, canActivate: [authGuard]},
  {path: "cars/:id/edit", component: EditCarComponent, canActivate: [authGuard]},
  {
    path: "cars/:id",
    component: CarComponent,
    resolve: [CarLogbookResolverService, UserCarsResolverService],
    canActivate: [authGuard]
  },
  {
    path: "cars/:id/car-logbook/:car-logbookId/car-logbook-posts/new",
    component: NewCarLogbookPostComponent,
    canActivate: [authGuard]
  },
  {
    path: "cars/:id/car-logbook/:car-logbookId/car-logbook-posts/:car-logbook-postId",
    component: CarLogbookPostComponent,
    canActivate: [authGuard]
  },
  {
    path: "cars/:id/car-logbook/:car-logbookId/car-logbook-posts/:car-logbook-postId/edit",
    component: EditCarLogbookPostComponent,
    canActivate: [authGuard]
  },

  {
    path: "car_clubs", component: CarClubsComponent,
    resolve: [CarClubsResolverService, UserCarClubsResolverService],
  },
  {path: "car_clubs/:id", component: CarClubComponent},
  {path: "car_clubs/new", component: NewCarClubComponent },

  {path: "**", component: ErrorComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}

import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {LoginComponent} from "./components/login/login.component";
import {ErrorComponent} from "./components/error/error.component";
import {WelcomeComponent} from "./components/welcome/welcome.component";
import {authGuard} from "./services/authServices/auth.guard";
import {LogoutComponent} from "./components/logout/logout.component";


const routes: Routes = [
  {path:"", component: LoginComponent},
  {path:"login", component: LoginComponent},
  {path:"welcome/:name", component: WelcomeComponent, canActivate: [authGuard]},
  {path:"logout", component: LogoutComponent, canActivate: [authGuard]},
  {path:"**", component: ErrorComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }

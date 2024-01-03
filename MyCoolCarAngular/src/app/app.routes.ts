import { Routes } from '@angular/router';
import {AddNewCarComponent} from "./components/add-new-car/add-new-car.component";
import {RegistrationComponent} from "./components/registration/registration.component";

export const routes: Routes = [
  {path: 'registration', component: RegistrationComponent},
  {path: 'main', component: AddNewCarComponent}
];

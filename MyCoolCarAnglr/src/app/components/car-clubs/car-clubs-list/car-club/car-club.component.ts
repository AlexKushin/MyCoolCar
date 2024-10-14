import {Component, Input} from '@angular/core';
import {RouterLink} from "@angular/router";
import {CarClub} from "../../../../models/carClub";

@Component({
  selector: 'app-car-club',
  standalone: true,
  imports: [
    RouterLink
  ],
  templateUrl: './car-club.component.html',
  styleUrl: './car-club.component.css'
})
export class CarClubComponent {
  @Input() carClub: CarClub;
}

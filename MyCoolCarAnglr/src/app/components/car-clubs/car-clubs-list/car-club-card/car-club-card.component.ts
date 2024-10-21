import {Component, Input} from '@angular/core';
import {RouterLink} from "@angular/router";
import {CarClub} from "../../../../models/carClub";

@Component({
  selector: 'app-car-club',
  standalone: true,
  imports: [
    RouterLink
  ],
  templateUrl: './car-club-card.component.html',
  styleUrl: './car-club-card.component.css'
})
export class CarClubCardComponent {
  @Input() carClub: CarClub;
}

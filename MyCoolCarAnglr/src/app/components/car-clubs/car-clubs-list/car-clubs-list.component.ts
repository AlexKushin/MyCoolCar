import {Component, Input} from '@angular/core';
import {CarClub} from "../../../models/carClub";
import {CarCardComponent} from "../../car/car-card/car-card.component";
import {NgForOf} from "@angular/common";
import {CarClubCardComponent} from "./car-club-card/car-club-card.component";

@Component({
  selector: 'app-car-clubs-list',
  standalone: true,
  imports: [
    CarCardComponent,
    NgForOf,
    CarClubCardComponent
  ],
  templateUrl: './car-clubs-list.component.html',
  styleUrl: './car-clubs-list.component.css'
})
export class CarClubsListComponent {
  @Input() carClubs: CarClub[];
  @Input() header: string;
}

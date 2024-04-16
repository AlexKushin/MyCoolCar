import {Component, Input} from '@angular/core';
import {Car} from "../../../../models/car";
import {CarCardComponent} from "../../car-card/car-card.component";
import {NgForOf} from "@angular/common";

@Component({
  selector: 'app-cars-list',
  standalone: true,
  imports: [
    CarCardComponent,
    NgForOf
  ],
  templateUrl: './cars-list.component.html',
  styleUrl: './cars-list.component.css'
})
export class CarsListComponent {
  @Input() cars: Car[];
  @Input() header: string;
}

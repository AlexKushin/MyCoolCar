import {Component, Input} from '@angular/core';
import {CommonModule} from '@angular/common';
import {Car} from "../../../models/car";
import {MatCardModule} from "@angular/material/card";
import {RouterLink} from "@angular/router";

@Component({
  selector: 'app-car-card',
  standalone: true,
  imports: [CommonModule, MatCardModule, RouterLink],
  templateUrl: './car-card.component.html',
  styleUrls: ['./car-card.component.css']
})
export class CarCardComponent {

  @Input() car: Car;


}

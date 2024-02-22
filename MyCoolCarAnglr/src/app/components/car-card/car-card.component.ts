import {Component, Input, OnDestroy, OnInit} from '@angular/core';
import { CommonModule } from '@angular/common';
import {Car} from "../../models/car";
import {MatCardModule} from "@angular/material/card";

@Component({
  selector: 'app-car-card',
  standalone: true,
  imports: [CommonModule,MatCardModule],
  templateUrl: './car-card.component.html',
  styleUrls: ['./car-card.component.css']
})
export class CarCardComponent {

  @Input() car!: Car;




}

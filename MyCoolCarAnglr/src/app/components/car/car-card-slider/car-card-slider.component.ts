import {Component, Input, OnDestroy, OnInit} from '@angular/core';
import { CommonModule } from '@angular/common';
import {Car} from "../../../models/car";
import {CarCardComponent} from "../car-card/car-card.component";

@Component({
  selector: 'app-car-card-slider',
  standalone: true,
  imports: [CommonModule, CarCardComponent],
  templateUrl: './car-card-slider.component.html',
  styleUrls: ['./car-card-slider.component.css']
})
export class CarCardSliderComponent implements OnInit, OnDestroy{

  @Input() cars: Car[] = [];
  currentIndex: number = 0;
  timeoutId?: number;

  ngOnInit(): void {
    this.resetTimer();
  }
  ngOnDestroy() {
    window.clearTimeout(this.timeoutId);
  }
  resetTimer() {
    if (this.timeoutId) {
      window.clearTimeout(this.timeoutId);
    }
    this.timeoutId = window.setTimeout(() => this.goToNext(), 3000);
  }

  goToPrevious(): void {
    const isFirstSlide = this.currentIndex === 0;
    const newIndex = isFirstSlide
      ? this.cars.length - 1
      : this.currentIndex - 1;

    this.resetTimer();
    this.currentIndex = newIndex;
  }

  goToNext(): void {
    const isLastSlide = this.currentIndex === this.cars.length - 1;
    const newIndex = isLastSlide ? 0 : this.currentIndex + 1;

    this.resetTimer();
    this.currentIndex = newIndex;
  }

  goToSlide(slideIndex: number): void {
    this.resetTimer();
    this.currentIndex = slideIndex;
  }

  getCurrentSlideUrl() {
    console.log('image url = '+this.cars[this.currentIndex].mainImageUrl)
    return  this.cars[this.currentIndex];
  }

}

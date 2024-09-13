import {Component, EventEmitter, Input, Output} from '@angular/core';

@Component({
  selector: 'app-warning',
  standalone: true,
  imports: [],
  templateUrl: './warning.component.html',
  styleUrl: './warning.component.css'
})
export class WarningComponent {
  @Input() message: string;
  @Output() close = new EventEmitter<void>();

  onClose() {
    this.close.emit();
  }
}

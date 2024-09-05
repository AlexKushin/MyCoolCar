import {Component, Input, OnInit} from '@angular/core';


@Component({
  selector: 'app-car-logbook-post',
  standalone: true,
  imports: [],
  templateUrl: './car-logbook-post.component.html',
  styleUrl: './car-logbook-post.component.css'
})
export class CarLogbookPostComponent implements OnInit{
  @Input() log_post: any

  ngOnInit(): void {
    console.log("log post:")
    console.log(this.log_post)
  }
}

import {Component, OnInit} from '@angular/core';
import { CommonModule } from '@angular/common';
import {ActivatedRoute} from "@angular/router";

@Component({
  selector: 'app-welcome',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './welcome.component.html',
  styleUrls: ['./welcome.component.css']
})
export class WelcomeComponent implements OnInit {


  message = "some welcome message"
  welcomeMessageFromService: string = ''
  name = ''

  constructor(
    private route: ActivatedRoute,
    // private service: WelcomeDataService
  ) {
  }

  ngOnInit(): void {
    console.log(this.route.snapshot.params["name"])
    this.name = this.route.snapshot.params["name"]
  }
}

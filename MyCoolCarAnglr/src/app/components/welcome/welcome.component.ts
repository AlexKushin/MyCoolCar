import {Component, OnInit} from '@angular/core';
import { CommonModule } from '@angular/common';
import {ActivatedRoute, Router} from "@angular/router";
import {UserService} from "../../services/user.service";
import {MatCardModule} from "@angular/material/card";
import {CarCardComponent} from "../car-card/car-card.component";

@Component({
  selector: 'app-welcome',
  standalone: true,
  imports: [CommonModule, MatCardModule, CarCardComponent],
  templateUrl: './welcome.component.html',
  styleUrls: ['./welcome.component.css']
})
export class WelcomeComponent implements OnInit {



  name = ''
  user: any

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private userService: UserService
  ) {
  }

  ngOnInit(): void {
    console.log(this.route.snapshot.params["name"])
    this.name = this.route.snapshot.params["name"]
    this.getUser()
  }
  addCar(){
    this.router.navigate(['cars'])
  }
  getUser(){
    this.userService.getCurrentUser().subscribe(
      response => {
      console.log(response)
      this.user = response;
    }
    )
  }
}

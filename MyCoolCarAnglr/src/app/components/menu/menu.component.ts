import {Component, OnInit} from '@angular/core';
import { CommonModule } from '@angular/common';
import {AuthenticationService} from "../../services/authServices/authentication.service";
import { RouterModule } from '@angular/router';
@Component({
  selector: 'app-menu',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './menu.component.html',
  styleUrls: ['./menu.component.css']
})
export class MenuComponent implements OnInit {

  // isUserLoggedIn : boolean = false;

  constructor(
    public authenticationService: AuthenticationService
     )
    {}

  ngOnInit(): void {
    //this.isUserLoggedIn = this.authenticationService.isUserLoggedIn();
  }

}

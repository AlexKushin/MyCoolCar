import {Component, OnInit} from '@angular/core';
import { CommonModule } from '@angular/common';
import {AuthenticationService} from "../../services/authServices/authentication.service";
import { RouterModule } from '@angular/router';
@Component({
  selector: 'app-menu',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {

  // isUserLoggedIn : boolean = false;

  constructor(
    public authenticationService: AuthenticationService
     )
    {}

  ngOnInit(): void {
    //this.isUserLoggedIn = this.authenticationService.isUserLoggedIn();
  }

}

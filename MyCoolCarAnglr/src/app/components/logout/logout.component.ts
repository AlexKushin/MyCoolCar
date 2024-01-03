import {Component, OnInit} from '@angular/core';
import { CommonModule } from '@angular/common';
import {AuthenticationService} from "../../services/authServices/authentication.service";

@Component({
  selector: 'app-logout',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './logout.component.html',
  styleUrls: ['./logout.component.css']
})
export class LogoutComponent implements OnInit{

  constructor(
   // private hardcodedAuthenticatedService: HardcodedAuthenticationService,
    private basicAuthService: AuthenticationService
  ){}


  ngOnInit(): void {
    // this.hardcodedAuthenticatedService.logout();
    this.basicAuthService.logout();
  }
}

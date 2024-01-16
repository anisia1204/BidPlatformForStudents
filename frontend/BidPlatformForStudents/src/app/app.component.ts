import {Component, inject, OnDestroy, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {DialogService, DynamicDialogRef} from "primeng/dynamicdialog";
import {RegistrationComponent} from "./auth/registration/registration.component";
import {LoginService} from "./auth/login/login.service";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
  providers:[DialogService]
})
export class AppComponent implements OnInit{
  constructor(private loginService: LoginService) {
  }
  ngOnInit(): void {
    this.loginService.autoLogin()
  }

}

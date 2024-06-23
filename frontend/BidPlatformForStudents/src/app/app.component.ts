import {Component, OnInit} from '@angular/core';
import {DialogService} from "primeng/dynamicdialog";
import {LoginService} from "./auth/login/login.service";
import {PrimeNGConfig} from "primeng/api";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
  providers:[DialogService]
})
export class AppComponent implements OnInit{
  constructor(private loginService: LoginService, private primengConfig: PrimeNGConfig) {
  }
  ngOnInit(): void {
    this.loginService.autoLogin()
    this.primengConfig.ripple = true
  }

}

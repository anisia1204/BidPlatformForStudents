import {Component, OnInit} from '@angular/core';
import {DialogService} from "primeng/dynamicdialog";
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

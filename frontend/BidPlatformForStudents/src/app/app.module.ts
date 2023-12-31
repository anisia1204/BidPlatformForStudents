import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppComponent } from './app.component';
import { LoginComponent } from './auth/login/login.component';
import {CardModule} from "primeng/card";
import {ButtonModule} from "primeng/button";
import {ReactiveFormsModule} from "@angular/forms";
import {ChipsModule} from "primeng/chips";
import { RouterLink, RouterModule, Routes} from "@angular/router";
import { RegistrationComponent } from './auth/registration/registration.component';
import {ToastModule} from "primeng/toast";
import {ConfirmDialogModule} from "primeng/confirmdialog";
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {CommonModule} from "@angular/common";
import {HttpClientModule} from "@angular/common/http";
import { NavbarComponent } from './infra/navbar/navbar.component';
import {MenubarModule} from "primeng/menubar";
import { DashboardComponent } from './dashboard/dashboard.component';

const routes: Routes = [
  {
    path: "",
    redirectTo: "login",
    pathMatch: "full",
  },
  {
    path: "login",
    component: LoginComponent
  },
  {
    path: "dashboard",
    component: DashboardComponent
  }
]

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    RegistrationComponent,
    NavbarComponent,
    DashboardComponent
  ],
    imports: [
        BrowserModule,
        CardModule,
        ButtonModule,
        ReactiveFormsModule,
        ChipsModule,
        RouterLink,
        RouterModule.forRoot(routes),
        ToastModule,
        ConfirmDialogModule,
        BrowserAnimationsModule,
        CommonModule,
        HttpClientModule,
        MenubarModule
    ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }

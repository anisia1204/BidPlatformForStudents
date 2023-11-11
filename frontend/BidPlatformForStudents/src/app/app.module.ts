import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppComponent } from './app.component';
import { LoginComponent } from './login/login.component';
import {CardModule} from "primeng/card";
import {ButtonModule} from "primeng/button";
import {ReactiveFormsModule} from "@angular/forms";
import {ChipsModule} from "primeng/chips";
import {Route, RouterLink, RouterModule} from "@angular/router";
import { RegistrationComponent } from './registration/registration.component';

const routes: Route[] = [
  {
    path: "",
    redirectTo: "",
    pathMatch: "full"
  },
  {
    path: "registration",
    component: RegistrationComponent
  }
]

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    RegistrationComponent
  ],
    imports: [
        BrowserModule,
        CardModule,
        ButtonModule,
        ReactiveFormsModule,
        ChipsModule,
        RouterLink,
        RouterModule.forRoot(routes)
    ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }

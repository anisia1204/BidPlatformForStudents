import {Component, inject, OnDestroy} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {DialogService, DynamicDialogRef} from "primeng/dynamicdialog";
import {RegistrationComponent} from "./auth/registration/registration.component";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
  providers:[DialogService]
})
export class AppComponent{
  route = inject(ActivatedRoute)
  router = inject(Router)
}

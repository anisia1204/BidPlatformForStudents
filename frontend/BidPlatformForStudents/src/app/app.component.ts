import {Component, inject, OnDestroy} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {DialogService, DynamicDialogRef} from "primeng/dynamicdialog";
import {RegistrationComponent} from "./registration/registration.component";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
  providers:[DialogService]
})
export class AppComponent implements OnDestroy{
  route = inject(ActivatedRoute)
  router = inject(Router)
  dialogService = inject(DialogService)
  dialog: DynamicDialogRef | undefined;


  register() {
    this.dialog = this.dialogService.open(RegistrationComponent, {
      header: "Inregistreaza-te!",
      width: "16%",
      height: "55%",
      position: "center"
    })
  }

  ngOnDestroy(): void {
    if (this.dialog) {
      this.dialog.close();
    }
  }
}

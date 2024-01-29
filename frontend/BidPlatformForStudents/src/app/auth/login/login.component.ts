import {Component, inject, OnDestroy} from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {LoginService} from "./login.service";
import {UserDtoModel} from "../domain/user-dto.model";
import {MessageService} from "primeng/api";
import {Router} from "@angular/router";
import {DialogService, DynamicDialogRef} from "primeng/dynamicdialog";
import {RegistrationComponent} from "../registration/registration.component";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss'],
  providers: [MessageService]
})
export class LoginComponent implements OnDestroy{
  form: any;
  loginService = inject(LoginService)
  userDto : UserDtoModel | undefined
  messageService = inject(MessageService)
  router = inject(Router)


  constructor() {
    this.createForm();
  }

  createForm() {
    this.form = new FormGroup<any | undefined>({
      email: new FormControl<string | any>('', [Validators.email, Validators.required]),
      password: new FormControl<string | any>('', Validators.required)
    })
  }

  onSubmit() {
    if(this.form.valid) {
      this.userDto = <UserDtoModel>this.form.value
      this.loginService.login(this.userDto).subscribe(
        loggedInUserDto => {
          this.loginService.isLoggedIn(loggedInUserDto);
          this.router.navigate(['/dashboard'])
        },
        errorResponse => {
          this.messageService.add({
            severity: 'error',
            summary: 'Logare esuata!',
            detail: this.handleErrorMessage(errorResponse)
          })
        }
      )
    }
  }

  private handleErrorMessage(errorResponse: any): string {
    let message = 'Nu v-ati putut loga!';
    console.log(errorResponse.error);

    if (errorResponse.status === 403) {
      message = 'Nu exista niciun cont inregistrat cu acest email!';
    } else if (errorResponse.error.password) {
      message = errorResponse.error.password;
    } else if (errorResponse.error.emailAlreadySent) {
      message = errorResponse.error.emailAlreadySent;
    }

    console.log(message);
    return message;
  }


  dialogService = inject(DialogService)
  dialog: DynamicDialogRef | undefined;


  register() {
    this.dialog = this.dialogService.open(RegistrationComponent, {
      header: "Inregistreaza-te!",
      width: "19%",
      height: "69%",
      position: "center"
    })
  }

  ngOnDestroy(): void {
    if (this.dialog) {
      this.dialog.close();
    }
  }
}

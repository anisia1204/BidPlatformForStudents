import {Component, inject} from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {LoginService} from "./login.service";
import {UserDtoModel} from "../domain/user-dto.model";
import {MessageService} from "primeng/api";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss'],
  providers: [MessageService]
})
export class LoginComponent {
  form: any;
  loginService = inject(LoginService)
  userDto : UserDtoModel = new UserDtoModel()
  messageService = inject(MessageService)

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
        userDto => console.log(userDto),
        errorResponse => {
          let message = "Nu v-ati putut loga!"
          console.log(errorResponse.error)
          if(errorResponse.status == 403)
            message = "Nu exista niciun cont inregistrat cu acest email!"
          else if(errorResponse.error.password)
            message = errorResponse.error.password
          console.log(message)
          this.messageService.add({
            severity: 'error',
            summary: 'Logare esuata!',
            detail: message
          })
        }
      )
    }
  }
}

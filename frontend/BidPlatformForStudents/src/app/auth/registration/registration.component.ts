import {Component, inject} from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {UserDtoModel} from "../domain/user-dto.model";
import {RegistrationService} from "./registration.service";
import {MessageService} from "primeng/api";

@Component({
  selector: 'app-registration',
  templateUrl: './registration.component.html',
  styleUrls: ['./registration.component.scss'],
  providers: [MessageService]
})
export class RegistrationComponent {
  form: any;
  userDto: UserDtoModel | undefined;
  registrationService = inject(RegistrationService)
  messageService = inject(MessageService)

  constructor() {
    this.createForm();
  }

  createForm() {
    this.form = new FormGroup<any>({
      lastName: new FormControl<string | undefined>("", Validators.required),
      firstName: new FormControl<string | undefined>("", Validators.required),
      email: new FormControl<string | undefined>("", [Validators.required, Validators.email]),
      password: new FormControl<string | undefined>("", Validators.required),
    })
  }

  submit() {
    if (this.form.valid) {
      this.userDto = <UserDtoModel>this.form.value
      this.registrationService.saveUser(this.userDto)
        .subscribe(
          userDto => {
            console.log(userDto)
            this.messageService.add({
              severity: 'info',
              summary: 'Inregistrare reusita!',
              detail: 'V-ati inregistrat cu succes!'
            })
          },
          errorResponse => {
            this.messageService.add({
              severity: 'error',
              summary: 'Inregistrare esuata!',
              detail: errorResponse.error.email
            })
          }
        )
    }
  }
}

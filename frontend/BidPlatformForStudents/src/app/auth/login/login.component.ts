import {Component, inject} from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {LoginService} from "./login.service";
import {UserDtoModel} from "../domain/user-dto.model";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent {
  form: any;
  loginService = inject(LoginService)
  userDto : UserDtoModel = new UserDtoModel()

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
        jwtToken => console.log(jwtToken)
      )
    }
  }
}

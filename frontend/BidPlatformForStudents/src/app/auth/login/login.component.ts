import { Component } from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent {
  form: any;

  constructor() {
    this.createForm();
  }

  createForm() {
    this.form = new FormGroup<any | undefined>({
      email: new FormControl<string | any>('', [Validators.email, Validators.required]),
      password: new FormControl<string | any>('', Validators.required)
    })
  }
}

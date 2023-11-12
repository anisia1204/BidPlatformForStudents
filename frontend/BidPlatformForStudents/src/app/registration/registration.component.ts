import { Component } from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";

@Component({
  selector: 'app-registration',
  templateUrl: './registration.component.html',
  styleUrls: ['./registration.component.scss']
})
export class RegistrationComponent {
  form: any;

  constructor() {
    this.createForm();
  }

  createForm(){
    this.form = new FormGroup<any>({
      lastName: new FormControl<string | undefined>("", Validators.required),
      firstName: new FormControl<string | undefined>("", Validators.required),
      email: new FormControl<string | undefined>("", [Validators.required, Validators.email]),
      password: new FormControl<string | undefined>("", Validators.required),
    })
  }

  submit() {

  }
}

import {Component, Input, OnInit} from '@angular/core';
import {FormControl, FormGroup} from "@angular/forms";

@Component({
  selector: 'app-teaching-material-form',
  templateUrl: './teaching-material-form.component.html',
  styleUrls: ['./teaching-material-form.component.scss']
})
export class TeachingMaterialFormComponent implements OnInit{
  @Input() form: FormGroup | any;

  ngOnInit() {
    this.createForm();
  }

  createForm() {
    this.form.addControl('name',  new FormControl<string | null>(''));
    this.form.addControl('author',  new FormControl<string | null>(''));
    this.form.addControl('edition',  new FormControl<number | null>(null));
  }


}

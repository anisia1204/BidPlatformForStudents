import {Component, Input, OnDestroy, OnInit} from '@angular/core';
import {FormGroup} from "@angular/forms";
import {TutoringTypeModel} from "../domain/tutoring-type.model";
import {SelectItem} from "primeng/api";

@Component({
  selector: 'app-tutoring-service-form',
  templateUrl: './tutoring-service-form.component.html',
  styleUrls: ['./tutoring-service-form.component.css']
})
export class TutoringServiceFormComponent implements OnInit, OnDestroy{
  @Input() form: FormGroup | any;
  tutoringTypes: SelectItem[] = [];
  minDateForStartDate: Date = new Date();
  minDateForEndDate: Date = new Date();

  ngOnInit(): void {
    if(this.form.get("tutoringType").value) {
      this.form.get("tutoringType").value =
        {
          label: this.form.get("tutoringType").value === 'REMOTE' ? 'Remote' : 'Fizic',
          value: this.form.get("tutoringType").value === 'REMOTE' ? 0 : 1
        }

    }

    this.minDateForStartDate = new Date()
    this.minDateForEndDate.setDate(this.minDateForStartDate.getDate() + 1)
    this.initTutoringTypes();
  }

  initTutoringTypes() {
    this.tutoringTypes = [
      {label: 'Remote', value: TutoringTypeModel.REMOTE},
      {label: 'Fizic', value: TutoringTypeModel.ONSITE}
    ]
  }

  ngOnDestroy(): void {
    this.form.reset(new FormGroup({}))
  }

}

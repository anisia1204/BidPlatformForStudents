import {Component, Input, OnInit} from '@angular/core';
import {FormArray, FormControl, FormGroup} from "@angular/forms";
import {SkillDtoModel} from "../domain/skill-dto.model";
import {TutoringTypeModel} from "../domain/tutoring-type.model";
import {SelectItem} from "primeng/api";

@Component({
  selector: 'app-tutoring-service-form',
  templateUrl: './tutoring-service-form.component.html',
  styleUrls: ['./tutoring-service-form.component.scss']
})
export class TutoringServiceFormComponent implements OnInit{
  @Input() form: FormGroup | any;
  tutoringTypes: SelectItem[] = [];
  selectedTutoringType: TutoringTypeModel | any;

  ngOnInit(): void {
    this.createForm();
    this.initTutoringTypes();
  }

  createForm() {
    this.form.addControl('subject', new FormControl<string | null>(''));
    this.form.addControl('startDate', new FormControl<string | null>(''));
    this.form.addControl('endDate', new FormControl<string | null>(''));
    this.form.addControl('hoursPerSession', new FormControl<number | null>(null));
    this.form.addControl('tutoringType', new FormControl<TutoringTypeModel | null>(null));
  }

  initTutoringTypes() {
    this.tutoringTypes = [
      {label: 'Remote', value: TutoringTypeModel.REMOTE},
      {label: 'Fizic', value: TutoringTypeModel.ONSITE}
    ]
  }

}

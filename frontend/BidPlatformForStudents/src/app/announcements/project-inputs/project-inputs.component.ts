import {Component, Input, OnInit} from '@angular/core';
import {FormArray, FormControl, FormGroup} from "@angular/forms";
import {SkillDtoModel} from "../domain/skill-dto.model";

@Component({
  selector: 'app-project-inputs',
  templateUrl: './project-inputs.component.html',
  styleUrls: ['./project-inputs.component.scss']
})
export class ProjectInputsComponent implements OnInit{
  @Input() form: FormGroup | any;

  ngOnInit(): void {
    this.createForm();
    this.requiredSkills.push(this.createFormGroupForRequiredSkills());
  }

  createForm() {
    this.form.addControl('domain',  new FormControl<string | null>(''));
    this.form.addControl('teamSize',  new FormControl<number | null>(null));
    this.form.addControl('requiredSkills',  new FormArray<SkillDtoModel[] | any>([]));
  }

  createFormGroupForRequiredSkills() {
    return new FormGroup({
      id: new FormControl<number | any>(null),
      projectId: new FormControl<number | any>(null),
      skill: new FormControl<string | any>(''),
      description: new FormControl<string | any>(''),
      skillPoints: new FormControl<number | any>(null)
    })
  }

  get requiredSkills() {
    return this.form.controls['requiredSkills'] as FormArray;
  }
}

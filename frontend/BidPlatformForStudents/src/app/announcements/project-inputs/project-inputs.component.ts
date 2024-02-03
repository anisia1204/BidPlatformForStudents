import {Component, inject, Input, OnDestroy, OnInit} from '@angular/core';
import {FormArray, FormControl, FormGroup, Validators} from "@angular/forms";
import {
  NewAnnouncementValidatorHandlerServiceTsService
} from "../validator/new-announcement-validator-handler.service.ts.service";

@Component({
  selector: 'app-project-inputs',
  templateUrl: './project-inputs.component.html',
  styleUrls: ['./project-inputs.component.scss']
})
export class ProjectInputsComponent implements OnInit, OnDestroy{
  @Input() form: FormGroup | any;
  @Input() totalPoints: number | undefined;
  newAnnouncementValidatorHandlerService = inject(NewAnnouncementValidatorHandlerServiceTsService)

  ngOnInit(): void {
    if(!this.requiredSkills.controls.length) {
      this.addSkill()
    }
  }

  createFormGroupForRequiredSkills() {
    return new FormGroup({
      id: new FormControl<number | any>(null),
      projectId: new FormControl<number | any>(null),
      skill: new FormControl<string | any>('', Validators.required),
      description: new FormControl<string | any>(''),
      skillPoints: new FormControl<number | any>(null, [Validators.required, this.newAnnouncementValidatorHandlerService.validatePointsSum.bind(this, this.totalPoints, this.requiredSkills)])
    })
  }

  get requiredSkills() {
    return this.form.controls['requiredSkills'] as FormArray;
  }

  addSkill() {
    this.requiredSkills.push(this.createFormGroupForRequiredSkills());
  }

  removeSkill(i: number) {
    this.requiredSkills.removeAt(i);
    this.checkIfSkillPointsSumCorrespondsToTotalPoints(i-1);
  }

  checkIfSkillPointsSumCorrespondsToTotalPoints(index: number){
    const validationResponse = this.newAnnouncementValidatorHandlerService.validatePointsSum(this.totalPoints, this.requiredSkills);
    if(validationResponse?.pointsSumExceeded) {
      this.requiredSkills.at(index).get('skillPoints')?.setErrors(validationResponse)
    }
  }

  ngOnDestroy(): void {
    this.form.reset(new FormGroup({}))
    this.requiredSkills.clear();
  }

}

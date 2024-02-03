import {Component, inject} from '@angular/core';
import {FormArray, FormControl, FormGroup, Validators} from "@angular/forms";
import {TeachingMaterialDtoModel} from "../domain/teaching-material-dto.model";
import {TutoringServiceDtoModel} from "../domain/tutoring-service-dto.model";
import {NewAnnouncementService} from "./new-announcement.service";
import {ProjectDtoModel} from "../domain/project-dto.model";
import {TutoringTypeModel} from "../domain/tutoring-type.model";
import {SkillDtoModel} from "../domain/skill-dto.model";
import {
  NewAnnouncementValidatorHandlerServiceTsService
} from "../validator/new-announcement-validator-handler.service.ts.service";


@Component({
  selector: 'app-new-announcement',
  templateUrl: './new-announcement.component.html',
  styleUrls: ['./new-announcement.component.scss']
})
export class NewAnnouncementComponent{
  form: FormGroup | any;
  newAnnouncementService = inject(NewAnnouncementService)
  newAnnouncementValidatorHandlerService = inject(NewAnnouncementValidatorHandlerServiceTsService)

  constructor() {
    this.createForm();
  }

  createForm() {
    this.form = new FormGroup({
      id: new FormControl<number | null>(null),
      userId: new FormControl<number | null>(null),
      title: new FormControl<string | null>('', Validators.required),
      description: new FormControl<string | null>('', Validators.required),
      points: new FormControl<string | null>('', Validators.required),
      announcementType: new FormControl<string>('', Validators.required),
      tutoringService: new FormGroup({
        subject: new FormControl<string | null>(''),
        startDate: new FormControl<string | null>(''),
        endDate: new FormControl<string | null>(''),
        hoursPerSession: new FormControl<number | null>(null),
        tutoringType: new FormControl<TutoringTypeModel | null>(null)
      }),
      project: new FormGroup({
        domain:  new FormControl<string | null>(''),
        teamSize:  new FormControl<number | null>(null),
        requiredSkills:  new FormArray<SkillDtoModel[] | any>([
          this.createFormGroupForRequiredSkills()
        ])
      }),
      teachingMaterial: new FormGroup({
        name:  new FormControl<string | null>(''),
        author: new FormControl<string | null>(''),
        edition: new FormControl<number | null>(null)
      })
    })
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

  onSubmit() {
    if (this.form.valid) {
      const announcementType = this.form.get('announcementType').value;
      switch (announcementType) {
        case 'teachingMaterial':
          this.saveTeachingMaterial(this.form);
          break;
        case 'tutoringService':
          this.saveTutoringService(this.form);
          break;
        case 'project':
          this.saveProject(this.form);
          break;
      }
    }
    else {
      this.form.markAllAsTouched();
    }
  }

  saveTeachingMaterial(form: FormGroup) {
    const teachingMaterialDto = <TeachingMaterialDtoModel>{
      ...form.value,
      name: form.get('teachingMaterial')?.get('name')?.value,
      author: form.get('teachingMaterial')?.get('author')?.value,
      edition: form.get('teachingMaterial')?.get('edition')?.value,
    };
    this.newAnnouncementService.saveTeachingMaterialDto(teachingMaterialDto).subscribe(
      teachingMaterialDto => console.log(teachingMaterialDto)
    )
  }

  private saveTutoringService(form: FormGroup) {
    const tutoringServiceDto = <TutoringServiceDtoModel>{
      ...form.value,
      subject: form.get('tutoringService')?.get('subject')?.value,
      startDate: form.get('tutoringService')?.get('startDate')?.value,
      endDate: form.get('tutoringService')?.get('endDate')?.value,
      hoursPerSession: form.get('tutoringService')?.get('hoursPerSession')?.value,
      tutoringType: form.get('tutoringService')?.get('tutoringType')?.value.value,
    };
    this.newAnnouncementService.saveTutoringServiceDto(tutoringServiceDto).subscribe(
      tutoringServiceDto => console.log(tutoringServiceDto)
    )
  }

  private saveProject(form: FormGroup) {
    const projectDto = <ProjectDtoModel>{
      ...form.value,
      domain: form.get('project')?.get('domain')?.value,
      teamSize: form.get('project')?.get('teamSize')?.value,
      requiredSkills: form.get('project')?.get('requiredSkills')?.value,
    };
    this.newAnnouncementService.saveProjectDto(projectDto).subscribe(
      projectDto => console.log(projectDto)
    )
  }

  onAnnouncementTypeChange() {
    const announcementType = this.form.get('announcementType').value;

    this.newAnnouncementValidatorHandlerService.updateValidatorsForTutoringService(this.form.get('tutoringService') as FormGroup, Validators.required)
    this.newAnnouncementValidatorHandlerService.updateValidatorsForProject(this.form.get('project') as FormGroup, Validators.required, this.form.get('points')?.value)

    switch (announcementType) {
      case 'teachingMaterial':
        this.newAnnouncementValidatorHandlerService.clearValidatorsForTutoringService(this.form.get('tutoringService') as FormGroup);
        this.newAnnouncementValidatorHandlerService.clearValidatorsForProject(this.form.get('project') as FormGroup);
        break;
      case 'tutoringService':
        this.newAnnouncementValidatorHandlerService.updateValidatorsForTutoringService(this.form.get('tutoringService') as FormGroup, Validators.required)
        this.newAnnouncementValidatorHandlerService.clearValidatorsForProject(this.form.get('project') as FormGroup);
        break;
      case 'project':
        this.newAnnouncementValidatorHandlerService.updateValidatorsForProject(this.form.get('project') as FormGroup, Validators.required, this.form.get('points')?.value)
        this.newAnnouncementValidatorHandlerService.clearValidatorsForTutoringService(this.form.get('tutoringService') as FormGroup);
        break;
      default:
        break;
    }
  }
}

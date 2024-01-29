import {Component, inject} from '@angular/core';
import {Router} from "@angular/router";
import { FormControl, FormGroup, Validators} from "@angular/forms";
import {TeachingMaterialDtoModel} from "../domain/teaching-material-dto.model";
import {TutoringServiceDtoModel} from "../domain/tutoring-service-dto.model";
import {NewAnnouncementService} from "./new-announcement.service";
import {ProjectDtoModel} from "../domain/project-dto.model";


@Component({
  selector: 'app-new-announcement',
  templateUrl: './new-announcement.component.html',
  styleUrls: ['./new-announcement.component.scss']
})
export class NewAnnouncementComponent {
  router = inject(Router)
  form: FormGroup | any;
  newAnnouncementService = inject(NewAnnouncementService)


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
      announcementType: new FormControl<string>(''),
      tutoringService: new FormGroup({}),
      project: new FormGroup({}),
      teachingMaterial: new FormGroup({})
    })
  }

  onSubmit() {
    if(this.form.valid) {
      const announcementType = this.form.get('announcementType').value;
      switch(announcementType) {
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
}

import {Component, inject, OnDestroy, OnInit, Optional} from '@angular/core';
import {FormArray, FormControl, FormGroup, Validators} from "@angular/forms";
import {TeachingMaterialDtoModel} from "../domain/teaching-material-dto.model";
import {TutoringServiceDtoModel} from "../domain/tutoring-service-dto.model";
import {AnnouncementFormService} from "./announcement-form.service";
import {ProjectDtoModel} from "../domain/project-dto.model";
import {TutoringTypeModel} from "../domain/tutoring-type.model";
import {SkillDtoModel} from "../domain/skill-dto.model";
import {
  NewAnnouncementValidatorHandlerServiceTsService
} from "../validator/new-announcement-validator-handler.service.ts.service";
import {ActivatedRoute, Router} from "@angular/router";
import {AttachmentDtoModel} from "../domain/attachment-dto.model";
import {Subject, takeUntil} from "rxjs";
import {GoBackService} from "../../utils/go-back.service";
import {MessageService} from "primeng/api";
import {DynamicDialogConfig, DynamicDialogRef} from "primeng/dynamicdialog";
import {SkillStatusModel} from "../domain/skill-status.model";


@Component({
  selector: 'app-announcement-form',
  templateUrl: './announcement-form.component.html',
  styleUrls: ['./announcement-form.component.scss'],
  providers: [MessageService, DynamicDialogRef]
})
export class  AnnouncementFormComponent implements OnInit, OnDestroy {
  form: FormGroup | any;
  id: string | null = null;
  isNew = false;
  attachments: File[] = []
  attachmentDTOs: AttachmentDtoModel[] | undefined = []
  destroy$: Subject<boolean> = new Subject<boolean>()

  announcementFormService = inject(AnnouncementFormService)
  newAnnouncementValidatorHandlerService = inject(NewAnnouncementValidatorHandlerServiceTsService)
  route = inject(ActivatedRoute)
  router = inject(Router)
  messageService = inject(MessageService)
  goBackService = inject(GoBackService)
  announcementType = ""

  constructor(@Optional() public config: DynamicDialogConfig) {
    this.createForm();
  }

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      this.id = params['id']
      this.announcementType = history.state.type
      if (this.id) {
        this.announcementFormService
          .getAnnouncementTemplate(this.id, this.announcementType)
          .pipe(takeUntil(this.destroy$))
          .subscribe(
            (announcementDTO: TutoringServiceDtoModel | TeachingMaterialDtoModel | ProjectDtoModel) => {
              this.form.patchValue(announcementDTO);
              this.patchValueOfCorrespondingAnnouncementType(announcementDTO);
              this.form.get('announcementType').disable()
              if(this.announcementType === 'project')
                this.form.get('points').disable();
            }
          );
      } else {
        this.isNew = true;
      }
    })
    }


  createForm() {
    this.form = new FormGroup({
      id: new FormControl<number | null>(null),
      userId: new FormControl<number | null>(null),
      title: new FormControl<string | null>('', Validators.required),
      description: new FormControl<string | null>('', Validators.required),
      points: new FormControl<string | null>(''),
      announcementType: new FormControl<string>('', Validators.required),
      tutoringService: new FormGroup({
        subject: new FormControl<string | null>(''),
        startDate: new FormControl<string | null>(''),
        endDate: new FormControl<string | null>(''),
        hoursPerSession: new FormControl<number | null>(null),
        tutoringType: new FormControl<TutoringTypeModel | null>(null)
      }),
      project: new FormGroup({
        domain: new FormControl<string | null>(''),
        teamSize: new FormControl<number | null>(null),
        requiredSkills: new FormArray<SkillDtoModel[] | any>([
          this.createFormGroupForRequiredSkills()
        ])
      }),
      teachingMaterial: new FormGroup({
        name: new FormControl<string | null>(''),
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
      skillPoints: new FormControl<number | any>(null),
      status: new FormControl<SkillStatusModel | null>(null)
    })
  }

  onSubmit() {
    if (this.form.valid) {
      const announcementType = this.form.get('announcementType').value;
      switch (announcementType) {
        case 'teachingMaterial':
          this.submitTeachingMaterial(this.form);
          break;
        case 'tutoringService':
          this.submitTutoringService(this.form);
          break;
        case 'project':
          this.submitProject(this.form);
          break;
      }
    } else {
      this.form.markAllAsTouched();
    }
  }

  submitTeachingMaterial(form: FormGroup) {
    const teachingMaterialDto = <TeachingMaterialDtoModel>{
      ...form.value,
      name: form.get('teachingMaterial')?.get('name')?.value,
      author: form.get('teachingMaterial')?.get('author')?.value,
      edition: form.get('teachingMaterial')?.get('edition')?.value,
    };
    if(this.id) {
      teachingMaterialDto.attachmentDTOs = this.attachmentDTOs
      this.editTeachingMaterial(teachingMaterialDto, this.attachments)
    }
    else {
      this.saveTeachingMaterial(teachingMaterialDto, this.attachments)
    }

  }

  submitTutoringService(form: FormGroup) {
    const tutoringServiceDto = <TutoringServiceDtoModel>{
      ...form.value,
      subject: form.get('tutoringService')?.get('subject')?.value,
      startDate: form.get('tutoringService')?.get('startDate')?.value,
      endDate: form.get('tutoringService')?.get('endDate')?.value,
      hoursPerSession: form.get('tutoringService')?.get('hoursPerSession')?.value,
      tutoringType: form.get('tutoringService')?.get('tutoringType')?.value.value,
    };
    if(this.id) {
      this.editTutoringService(tutoringServiceDto)
    }
    else {
      this.saveTutoringService(tutoringServiceDto)
    }
  }

  submitProject(form: FormGroup) {
    const projectDto = <ProjectDtoModel>{
      ...form.value,
      domain: form.get('project')?.get('domain')?.value,
      teamSize: form.get('project')?.get('teamSize')?.value,
      requiredSkills: form.get('project')?.get('requiredSkills')?.value,
    };
    if(this.id) {
      this.editProject(projectDto)
    }
    else {
      this.saveProject(projectDto)
    }
  }

  onAnnouncementTypeChange() {
    const announcementType = this.form.get('announcementType').value;

    this.newAnnouncementValidatorHandlerService.updatePointsValidator(this.form.get('points'), Validators.required);
    this.newAnnouncementValidatorHandlerService.updateValidatorsForTutoringService(this.form.get('tutoringService') as FormGroup, Validators.required)
    this.newAnnouncementValidatorHandlerService.updateValidatorsForProject(this.form.get('project') as FormGroup, Validators.required)

    switch (announcementType) {
      case 'teachingMaterial':
        this.newAnnouncementValidatorHandlerService.updatePointsValidator(this.form.get('points'), Validators.required);
        this.newAnnouncementValidatorHandlerService.clearValidatorsForTutoringService(this.form.get('tutoringService') as FormGroup);
        this.newAnnouncementValidatorHandlerService.clearValidatorsForProject(this.form.get('project') as FormGroup);
        break;
      case 'tutoringService':
        this.newAnnouncementValidatorHandlerService.updatePointsValidator(this.form.get('points'), Validators.required);
        this.newAnnouncementValidatorHandlerService.updateValidatorsForTutoringService(this.form.get('tutoringService') as FormGroup, Validators.required)
        this.newAnnouncementValidatorHandlerService.clearValidatorsForProject(this.form.get('project') as FormGroup);
        break;
      case 'project':
        this.newAnnouncementValidatorHandlerService.clearPointsValidator(this.form.get('points'));
        this.newAnnouncementValidatorHandlerService.updateValidatorsForProject(this.form.get('project') as FormGroup, Validators.required)
        this.newAnnouncementValidatorHandlerService.clearValidatorsForTutoringService(this.form.get('tutoringService') as FormGroup);
        break;
      default:
        break;
    }
  }

  private patchValueOfCorrespondingAnnouncementType(announcementDTO: TutoringServiceDtoModel | TeachingMaterialDtoModel | ProjectDtoModel) {
    switch (announcementDTO.announcementType) {
      case 'teachingMaterial':
        const teachingMaterialDTO = announcementDTO as TeachingMaterialDtoModel
        this.form.get('teachingMaterial').patchValue(teachingMaterialDTO)
        this.attachmentDTOs = teachingMaterialDTO.attachmentDTOs
        break;
      case 'tutoringService':
        const tutoringServiceDTO = announcementDTO as TutoringServiceDtoModel
        this.form.get('tutoringService').patchValue(tutoringServiceDTO)
        break;
      case 'project':
        const projectDTO = announcementDTO as ProjectDtoModel
        if (projectDTO.requiredSkills?.length && projectDTO.requiredSkills?.length > 1) {
          projectDTO.requiredSkills.forEach(skill => {
            this.form.get('project').get('requiredSkills').push(this.createFormGroupForRequiredSkills())
          })
          this.form.get('project').get('requiredSkills').removeAt(projectDTO.requiredSkills?.length - 1)
        }
        this.form.get('project').patchValue(projectDTO)
        break;
      default:
        break;
    }
  }

  saveTeachingMaterial(teachingMaterialDto: TeachingMaterialDtoModel, files: File[]) {
    this.announcementFormService.saveTeachingMaterialDto(teachingMaterialDto, files).subscribe(
        teachingMaterialDto => this.config.data.ref.close(teachingMaterialDto)
    )
  }
  editTeachingMaterial(teachingMaterialDto: TeachingMaterialDtoModel, files: File[]) {
    this.announcementFormService.updateTeachingMaterialDto(teachingMaterialDto, files).subscribe(
      teachingMaterialDto => {
        this.goBackService.goBack(teachingMaterialDto)
      },
      error => {
        this.showErrorMessage(error.error.status)
      }
    )
  }
  onAttachmentsUploaded(attachments: File[]) {
    this.attachments = [...attachments]
  }
  onDeleteAttachmentDTO(id: number) {
    this.attachmentDTOs = this.attachmentDTOs?.filter(attachment => attachment.id !== id);
  }

  saveTutoringService(tutoringServiceDto: TutoringServiceDtoModel) {
    this.announcementFormService.saveTutoringServiceDto(tutoringServiceDto).subscribe(
        tutoringServiceDto => this.config.data.ref.close(tutoringServiceDto)
    )
  }
  editTutoringService(tutoringServiceDto: TutoringServiceDtoModel) {
    this.announcementFormService.updateTutoringServiceDto(tutoringServiceDto).subscribe(
      tutoringServiceDto => {
        this.goBackService.goBack(tutoringServiceDto)
      },
      error => {
        this.showErrorMessage(error.error.status)
      }
    )
  }

  saveProject(projectDto: ProjectDtoModel) {
    this.announcementFormService.saveProjectDto(projectDto).subscribe(
        projectDto => this.config.data.ref.close(projectDto)
    )
  }
  editProject(projectDto: ProjectDtoModel) {
    this.announcementFormService.updateProjectDto(projectDto).subscribe(
      () => {
        this.goBackService.goBack(projectDto)
      },
      error => {
        this.showErrorMessage(error.error.status)
      }
    )
  }
  private showErrorMessage(message: string) {
    this.messageService.add({
      severity: 'error',
      summary: 'Eroare',
      detail: message
    })
  }
  ngOnDestroy(): void {
    this.destroy$.next(true)
  }
}

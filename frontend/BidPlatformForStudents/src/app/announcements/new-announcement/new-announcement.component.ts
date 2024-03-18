import {Component, inject, OnDestroy, OnInit} from '@angular/core';
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
import {ActivatedRoute, Router} from "@angular/router";
import {AttachmentDtoModel} from "../domain/attachment-dto.model";
import {Subject, takeUntil} from "rxjs";
import {GoBackService} from "../../utils/go-back.service";
import {MessageService} from "primeng/api";
import {DialogService, DynamicDialogRef} from "primeng/dynamicdialog";


@Component({
  selector: 'app-new-announcement',
  templateUrl: './new-announcement.component.html',
  styleUrls: ['./new-announcement.component.scss'],
  providers: [MessageService, DynamicDialogRef]
})
export class NewAnnouncementComponent implements OnInit, OnDestroy {
  form: FormGroup | any;
  id: string | null = null;
  isNew = false;
  attachments: File[] = []
  attachmentDTOs: AttachmentDtoModel[] | undefined = []
  destroy$: Subject<boolean> = new Subject<boolean>()

  newAnnouncementService = inject(NewAnnouncementService)
  newAnnouncementValidatorHandlerService = inject(NewAnnouncementValidatorHandlerServiceTsService)
  route = inject(ActivatedRoute)
  router = inject(Router)
  messageService = inject(MessageService)
  goBackService = inject(GoBackService)
  dynamicDialogRef = inject(DynamicDialogRef)
  announcementType = ""

  constructor() {
    this.createForm();
  }

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      this.id = params['id']
      this.announcementType = history.state.type
      if (this.id) {
        this.newAnnouncementService
          .getAnnouncementTemplate(this.id, this.announcementType)
          .pipe(takeUntil(this.destroy$))
          .subscribe(
            (announcementDTO: TutoringServiceDtoModel | TeachingMaterialDtoModel | ProjectDtoModel) => {
              console.log(announcementDTO)
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
      skillPoints: new FormControl<number | any>(null)
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
    this.newAnnouncementService.saveTeachingMaterialDto(teachingMaterialDto, files).subscribe(
        teachingMaterialDto => this.dynamicDialogRef?.close(true)
    )
  }
  editTeachingMaterial(teachingMaterialDto: TeachingMaterialDtoModel, files: File[]) {
    this.newAnnouncementService.updateTeachingMaterialDto(teachingMaterialDto, files).subscribe(
      teachingMaterialDto => {
        this.goBackService.goBack(teachingMaterialDto)
      },
      error => {
        this.showErrorMessage()
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
    this.newAnnouncementService.saveTutoringServiceDto(tutoringServiceDto).subscribe(
        tutoringServiceDto => this.dynamicDialogRef?.close(true)
    )
  }
  editTutoringService(tutoringServiceDto: TutoringServiceDtoModel) {
    this.newAnnouncementService.updateTutoringServiceDto(tutoringServiceDto).subscribe(
      tutoringServiceDto => {
        this.goBackService.goBack(tutoringServiceDto)
      },
      error => {
        this.showErrorMessage()
      }
    )
  }

  saveProject(projectDto: ProjectDtoModel) {
    this.newAnnouncementService.saveProjectDto(projectDto).subscribe(
        projectDto => this.dynamicDialogRef?.close(true)
    )
  }
  editProject(projectDto: ProjectDtoModel) {
    this.newAnnouncementService.updateProjectDto(projectDto).subscribe(
      () => {
        this.goBackService.goBack(projectDto)
      },
      error => {
        this.showErrorMessage()
      }
    )
  }
  private showErrorMessage() {
    this.messageService.add({
      severity: 'error',
      summary: 'Eroare',
      detail: 'Nu ati putut edita anuntul!'
    })
  }
  ngOnDestroy(): void {
    this.destroy$.next(true)
  }
}

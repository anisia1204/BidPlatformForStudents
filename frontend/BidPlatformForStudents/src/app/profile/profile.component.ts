import {Component, inject, OnDestroy, OnInit} from '@angular/core';
import {ProfileService} from "./profile.service";
import {Subject, takeUntil} from "rxjs";
import {GoBackService} from "../utils/go-back.service";
import {UserVoModel} from "../auth/domain/user-vo.model";
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {UserDtoModel} from "../auth/domain/user-dto.model";
import {FileRemoveEvent, FileUploadHandlerEvent} from "primeng/fileupload";

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit, OnDestroy{
  editMode = false;
  editProfilePictureMode = false
  profileService = inject(ProfileService)
  goBackService = inject(GoBackService)
  userVo = new UserVoModel()
  destroy$: Subject<boolean> = new Subject<boolean>()
  form: any;

  soldAnnouncements: any;
  createdAnnouncements: any;
  labels = ['Proiecte', 'Materiale didactice', 'Servicii de tutorat']

  options: any;
  ngOnInit(): void {
    const documentStyle = getComputedStyle(document.documentElement);
    const textColor = documentStyle.getPropertyValue('--text-color');

    this.profileService
      .getChartData()
      .pipe(takeUntil(this.destroy$))
      .subscribe(res => {
        this.createdAnnouncements = {
          labels: this.labels,
          datasets: [
            {
              data: [
                res.createdAnnouncementsVO?.projects,
                res.createdAnnouncementsVO?.teachingMaterials,
                res.createdAnnouncementsVO?.tutoringServices
              ],
              backgroundColor: [documentStyle.getPropertyValue('--purple-200'), documentStyle.getPropertyValue('--pink-200'), documentStyle.getPropertyValue('--green-200')],
              hoverBackgroundColor: [documentStyle.getPropertyValue('--purple-400'), documentStyle.getPropertyValue('--pink-400'), documentStyle.getPropertyValue('--green-400')]
            }
          ]
        };

        this.soldAnnouncements = {
          labels: this.labels,
          datasets: [
            {
              data: [
                res.soldAnnouncementsVO?.projects,
                res.soldAnnouncementsVO?.teachingMaterials,
                res.soldAnnouncementsVO?.tutoringServices
              ],
              backgroundColor: [documentStyle.getPropertyValue('--blue-200'), documentStyle.getPropertyValue('--orange-200'), documentStyle.getPropertyValue('--red-200')],
              hoverBackgroundColor: [documentStyle.getPropertyValue('--blue-400'), documentStyle.getPropertyValue('--orange-400'), documentStyle.getPropertyValue('--red-400')]
            }
          ]
        };
      })

    this.options = {
      cutout: '60%',
      plugins: {
        legend: {
          labels: {
            color: textColor
          }
        }
      }
    };

    this.profileService
      .getProfileDetails()
      .pipe(takeUntil(this.destroy$))
      .subscribe(res => {
        this.userVo = res
      })
  }

  onEditProfile() {
    this.editMode = true
    this.form = new FormGroup<any>({
      lastName: new FormControl<string | undefined>(this.userVo.lastName, Validators.required),
      firstName: new FormControl<string | undefined>(this.userVo.firstName, Validators.required)
    })
  }

  onEditCancel() {
    this.editMode = false
  }

  onSubmit() {
    if(this.form.valid) {
      const userDto = <UserDtoModel>this.form.value
      this.profileService
        .editProfileDetails(userDto)
        .subscribe(res =>{
          this.userVo = {
            ...this.userVo,
            firstName: res.firstName,
            lastName: res.lastName
          }
          this.onEditCancel()
        })
    }
  }

  profilePicture: File[] =[]

  onRemove(event: FileRemoveEvent) {
    const removedFile: File = event.file;
    this.profilePicture = this.profilePicture?.filter(file => file !== removedFile);
  }

  uploadHandler(event: FileUploadHandlerEvent) {
    this.profilePicture = event.files as File[]
  }

  onUploadProfilePicture() {
    this.profileService
      .updateUserProfilePic(this.profilePicture)
      .pipe(takeUntil(this.destroy$))
      .subscribe(res => {
        if(this.userVo.profilePictureVO) {
          this.userVo.profilePictureVO.id = res.id
          this.userVo.profilePictureVO.base64EncodedStringOfFileContent = res.base64EncodedStringOfFileContent
          this.editProfilePictureMode = false
        }
      })
  }

  ngOnDestroy(): void {
    this.destroy$.next(true)
  }
}

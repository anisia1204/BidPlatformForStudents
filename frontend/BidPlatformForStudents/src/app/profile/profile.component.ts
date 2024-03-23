import {Component, inject, OnDestroy, OnInit} from '@angular/core';
import {ProfileService} from "./profile.service";
import {Subject, takeUntil} from "rxjs";
import {GoBackService} from "../utils/go-back.service";
import {UserVoModel} from "../auth/domain/user-vo.model";
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {UserDtoModel} from "../auth/domain/user-dto.model";

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.scss']
})
export class ProfileComponent implements OnInit, OnDestroy{
  editMode = false;
  profileService = inject(ProfileService)
  goBackService = inject(GoBackService)
  userVo = new UserVoModel()
  destroy$: Subject<boolean> = new Subject<boolean>()
  form: any;

  ngOnInit(): void {
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

  ngOnDestroy(): void {
    this.destroy$.next(true)
  }
}

import {Component, inject, OnDestroy, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {Subject, takeUntil} from "rxjs";
import {UpdateUserPointsService} from "./update-user-points.service";
import {UserDetailsVoModel} from "../domain/user-details-vo.model";
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {UpdateUserPointsDtoModel} from "../domain/update-user-points-dto.model";
import {MessageService} from "primeng/api";

@Component({
  selector: 'app-update-user-points',
  templateUrl: './update-user-points.component.html',
  styleUrls: ['./update-user-points.component.css'],
  providers: [MessageService]
})
export class UpdateUserPointsComponent implements OnInit, OnDestroy{
  activatedRoute = inject(ActivatedRoute)
  router = inject(Router)
  updateUserPointsService = inject(UpdateUserPointsService)
  messageService = inject(MessageService)
  userId: string | null = null
  userDetailsVo = new UserDetailsVoModel()
  destroy$: Subject<boolean> = new Subject<boolean>()
  form: any
  constructor() {
    this.createForm()
  }
  ngOnInit(): void {
    this.userId = this.activatedRoute.snapshot.params['userId']

    this.updateUserPointsService.getUserDetails(this.userId)
      .pipe(takeUntil(this.destroy$))
      .subscribe(res => this.userDetailsVo = res)
  }

  createForm() {
    this.form = new FormGroup({
      id: new FormControl<number | null>(null),
      pointsToWithdraw:  new FormControl<number | null>(null, [Validators.required]),
      currentSold:  new FormControl<number | null>(null)
    })
  }

  onSubmit() {
    if(this.form.valid) {
      const updateUserPointsDto = new UpdateUserPointsDtoModel()
      updateUserPointsDto.pointsToWithdraw = this.form.value.pointsToWithdraw
      updateUserPointsDto.currentSold = this.userDetailsVo.points
      updateUserPointsDto.id = this.userDetailsVo.id
      this.updateUserPointsService.updateUserPoints(updateUserPointsDto)
          .subscribe(res => {
            this.form.reset()
            this.userDetailsVo.points = res.currentSold
          },
          (error) => {
            this.messageService.add({ severity: 'error', summary: 'Eroare la retragere', detail: error.error.pointsToWithdraw });
          })
    }
  }

  onCancel() {
    this.router.navigate(['/scan-qr'])
  }

  ngOnDestroy(): void {
    this.destroy$.next(true)
  }
}

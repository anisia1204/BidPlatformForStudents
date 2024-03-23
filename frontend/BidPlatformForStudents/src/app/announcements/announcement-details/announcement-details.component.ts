import {Component, inject, OnDestroy, OnInit} from '@angular/core';
import {AnnouncementDetailsService} from "./announcement-details.service";
import {AnnouncementVoModel} from "../domain/announcement-vo.model";
import {ActivatedRoute} from "@angular/router";
import {Subject, takeUntil} from "rxjs";
import {TransactionDtoModel} from "../../transactions/domain/transaction-dto.model";
import {GoBackService} from "../../utils/go-back.service";

@Component({
  selector: 'app-announcement-details',
  templateUrl: './announcement-details.component.html',
  styleUrls: ['./announcement-details.component.scss']
})
export class AnnouncementDetailsComponent implements OnInit, OnDestroy{
  announcementDetailsService = inject(AnnouncementDetailsService)
  route = inject(ActivatedRoute)
  goBackService = inject(GoBackService)
  announcement: AnnouncementVoModel | null = null;
  announcementType: string | null = null;
  myAnnouncements: boolean = false;
  id: number | null = null
  destroy$: Subject<boolean> = new Subject<boolean>()
  ngOnInit(): void {
    this.route.params.subscribe(params => {
      this.announcementType = history.state.type
      this.id = history.state.id
      this.myAnnouncements = history.state.myAnnouncements
      this.announcementDetailsService
        .getDetails(this.id)
        .pipe(takeUntil(this.destroy$))
        .subscribe(announcementVO => this.announcement = announcementVO)
    })
  }
  ngOnDestroy(): void {
    this.destroy$.next(true)
  }
}

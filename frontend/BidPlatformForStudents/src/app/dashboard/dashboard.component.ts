import {Component, inject, OnDestroy, OnInit} from '@angular/core';
import {GoBackService} from "../utils/go-back.service";
import {Router} from "@angular/router";
import {AnnouncementVoModel} from "../announcements/domain/announcement-vo.model";
import {Subject, takeUntil} from "rxjs";
import {DashboardService} from "./dashboard.service";
import {MessageService} from "primeng/api";
import {TransactionDtoModel} from "../transactions/domain/transaction-dto.model";

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss'],
  providers: [MessageService]
})
export class DashboardComponent implements OnInit, OnDestroy{
  dashboardService = inject(DashboardService)
  goBackService = inject(GoBackService)
  router = inject(Router)
  totalRecords: number | undefined;
  size = 9;
  sort = ['status'];
  announcements: AnnouncementVoModel[] = []
  destroy$: Subject<boolean> = new Subject<boolean>()
  lazyLoadEvent: {page: number, size: number} = {page: 0, size: this.size};

  ngOnInit(): void {
    this.goBackService
      .getNavData()
      .pipe(takeUntil(this.destroy$))
      .subscribe(() => {
        this.onLazyLoad(this.lazyLoadEvent)
      })
  }
  onLazyLoad(lazyLoadEvent: {page: number, size: number}) {
    this.lazyLoadEvent = lazyLoadEvent
    this.dashboardService.getDashboardAnnouncements(lazyLoadEvent.page,lazyLoadEvent.size, this.sort).subscribe(
      announcements => {
        this.announcements = announcements.content
        this.totalRecords = announcements.totalElements;
      }
    )
  }
  onBuyProject($event: number) {

  }

  onBuy(announcementId: number) {
    const transactionDto = new TransactionDtoModel()
    transactionDto.announcementId = announcementId
    this.dashboardService.buy(transactionDto).subscribe(
      res => this.goBackService.goBack(transactionDto)
    )
  }

  ngOnDestroy(): void {
    this.destroy$.next(true)
  }
}

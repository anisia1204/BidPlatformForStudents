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
  messageService = inject(MessageService)

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
      res => {
        this.onLazyLoad(this.lazyLoadEvent)
        this.messageService.add({ severity: 'info', summary: 'Success', detail: 'Anunt cumparat cu succes' });
      },
      errorResponse => {
        this.messageService.add({
          severity: 'error',
          summary: 'Tranzactie esuata!',
          detail: this.handleErrorMessage(errorResponse)
        })
      }
    )
  }
  private handleErrorMessage(errorResponse: any) {
    let message = 'Nu ati putut cumpara anuntul!';

    if (errorResponse.error.amount) {
      message = errorResponse.error.amount;
    } else if (errorResponse.error.announcementId) {
      message = errorResponse.error.announcementId;
    } else if (errorResponse.error.skillIds) {
      message = errorResponse.error.skillIds;
    }

    return message;
  }
  ngOnDestroy(): void {
    this.destroy$.next(true)
  }


}

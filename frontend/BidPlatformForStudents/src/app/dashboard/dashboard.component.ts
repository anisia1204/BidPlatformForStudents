import {Component, inject, Input, OnDestroy, OnInit} from '@angular/core';
import {GoBackService} from "../utils/go-back.service";
import {Router} from "@angular/router";
import {AnnouncementVoModel} from "../announcements/domain/announcement-vo.model";
import {Subject, takeUntil} from "rxjs";
import {DashboardService} from "./dashboard.service";
import {MessageService} from "primeng/api";
import {TransactionDtoModel} from "../transactions/domain/transaction-dto.model";
import {FavoriteAnnouncementDtoModel} from "../announcements/domain/favorite-announcement-dto.model";
import {AnnouncementListFilters} from "../utils/announcement-list/announcement-list-filters";
import {notMyAnnouncementCols} from "../utils/announcement-list/not-my-announcements-llist-filter-columns";
import {AnnouncementSortData} from "../utils/announcement-list/announcement-sort-data";

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css'],
  providers: [MessageService]
})
export class DashboardComponent implements OnInit, OnDestroy{
  @Input() favoritesTitle: string | null = null
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

  @Input() cols = notMyAnnouncementCols

  ngOnInit(): void {
    this.goBackService
      .getNavData()
      .pipe(takeUntil(this.destroy$))
      .subscribe(() => {
        this.onLazyLoad(this.lazyLoadEvent)
      })
  }
  onLazyLoad(lazyLoadEvent: {page: number, size: number, filters?: AnnouncementListFilters, sort?: AnnouncementSortData}) {
    this.lazyLoadEvent = lazyLoadEvent
    if(!this.favoritesTitle) {
      this.dashboardService.getDashboardAnnouncements(lazyLoadEvent.page,lazyLoadEvent.size, lazyLoadEvent.filters, lazyLoadEvent.sort).subscribe(
        announcements => {
          this.announcements = announcements.content
          this.totalRecords = announcements.totalElements;
        }
      )
    }
    else {
      this.dashboardService.getFavoriteAnnouncements(lazyLoadEvent.page,lazyLoadEvent.size, lazyLoadEvent.filters, lazyLoadEvent.sort).subscribe(
        announcements => {
          this.announcements = announcements.content
          this.totalRecords = announcements.totalElements;
        }
      )
    }
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

  onAddToFavourites(favoriteAnnouncementDto: FavoriteAnnouncementDtoModel) {
    this.dashboardService.addToFavorites(favoriteAnnouncementDto)
      .subscribe(res => this.onLazyLoad(this.lazyLoadEvent))
  }
  onRemoveFromFavorites(favoriteAnnouncementId: number) {
    this.dashboardService.removeFromFavorites(favoriteAnnouncementId)
      .subscribe(res => this.onLazyLoad(this.lazyLoadEvent))
  }
  ngOnDestroy(): void {
    this.destroy$.next(true)
  }
}

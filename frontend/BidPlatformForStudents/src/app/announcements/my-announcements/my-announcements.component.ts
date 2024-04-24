import {Component, inject, OnDestroy, OnInit} from '@angular/core';
import {MyAnnouncementsService} from "./my-announcements.service";
import {AnnouncementVoModel} from "../domain/announcement-vo.model";
import {GoBackService} from "../../utils/go-back.service";
import {Subject, takeUntil} from "rxjs";
import {Router} from "@angular/router";
import {MessageService} from "primeng/api";
import {AnnouncementListFilters} from "../../utils/announcement-list/announcement-list-filters";
import {cols} from "../../utils/announcement-list/announcement-list-filter-columns";
import {AnnouncementSortData} from "../../utils/announcement-list/announcement-sort-data";

@Component({
  selector: 'app-my-announcements',
  templateUrl: './my-announcements.component.html',
  styleUrls: ['./my-announcements.component.scss'],
  providers: [MessageService]
})
export class MyAnnouncementsComponent implements OnInit, OnDestroy{
  myAnnouncementsService = inject(MyAnnouncementsService)
  goBackService = inject(GoBackService)
  router = inject(Router)
  messageService = inject(MessageService)

  totalRecords: number | undefined;
  size = 9;
  sort = ['createdAt'];
  announcements: AnnouncementVoModel[] = []
  destroy$: Subject<boolean> = new Subject<boolean>()
  lazyLoadEvent: {page: number, size: number} = {page: 0, size: this.size};

  cols = cols

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
    this.myAnnouncementsService.getMyAnnouncements(lazyLoadEvent.page,lazyLoadEvent.size, lazyLoadEvent.filters, lazyLoadEvent.sort).subscribe(
      announcements => {
        this.announcements = announcements.content
        this.totalRecords = announcements.totalElements;
      }
    )
  }

  onDelete(id: number) {
    this.myAnnouncementsService.delete(id).subscribe(
      () => {
        this.onLazyLoad(this.lazyLoadEvent)
        this.messageService.add({ severity: 'info', summary: 'Success', detail: 'Anunt sters cu succes' });
      }
    )
  }

  ngOnDestroy(): void {
    this.destroy$.next(true)
  }
}

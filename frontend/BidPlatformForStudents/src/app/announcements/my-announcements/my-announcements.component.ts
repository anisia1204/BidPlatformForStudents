import {Component, inject, OnDestroy, OnInit} from '@angular/core';
import {MyAnnouncementsService} from "./my-announcements.service";
import {AnnouncementVoModel} from "../domain/announcement-vo.model";
import {GoBackService} from "../../utils/go-back.service";
import {Subject, takeUntil} from "rxjs";
import {MessageService} from "primeng/api";
import {Router} from "@angular/router";

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
  size = 10;
  sort = ['createdAt'];
  announcements: AnnouncementVoModel[] = []
  destroy$: Subject<boolean> = new Subject<boolean>()

  ngOnInit(): void {
    this.goBackService.back$
      .pipe(takeUntil(this.destroy$))
      .subscribe((message) => {
      this.router.navigate(['/my-announcements'])
      this.messageService.add({
        severity: 'info',
        summary: 'Success',
        detail: message
      })
    })
  }
  onLazyLoad(lazyLoadEvent: {page: number, size: number}) {
    this.myAnnouncementsService.getMyAnnouncements(lazyLoadEvent.page,lazyLoadEvent.size, this.sort).subscribe(
      announcements => {
        this.announcements = announcements.content
        this.totalRecords = announcements.totalElements;
      }
    )
  }

  onDelete(id: number) {
    this.myAnnouncementsService.delete(id).subscribe(
      res => console.log(res)
    )
  }

  ngOnDestroy(): void {
    this.destroy$.next(true)
  }
}

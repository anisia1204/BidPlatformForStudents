import {Component, inject, OnInit} from '@angular/core';
import {MyAnnouncementsService} from "./my-announcements.service";
import {AnnouncementVoModel} from "../domain/announcement-vo.model";

@Component({
  selector: 'app-my-announcements',
  templateUrl: './my-announcements.component.html',
  styleUrls: ['./my-announcements.component.scss']
})
export class MyAnnouncementsComponent{
  myAnnouncementsService = inject(MyAnnouncementsService)

  totalRecords: number | undefined;
  size = 10;
  sort = ['createdAt'];
  announcements: AnnouncementVoModel[] = []

  onLazyLoad(lazyLoadEvent: {page: number, size: number}) {
    this.myAnnouncementsService.getMyAnnouncements(lazyLoadEvent.page,lazyLoadEvent.size, this.sort).subscribe(
      announcements => {
        this.announcements = announcements.content
        this.totalRecords = announcements.totalElements;
      }
    )
  }
}

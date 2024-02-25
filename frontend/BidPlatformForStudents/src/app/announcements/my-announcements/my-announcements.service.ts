import {inject, Injectable} from "@angular/core";
import {AnnouncementResourceService} from "../resource-service/announcement-resource.service";

@Injectable({
    providedIn: 'root'
})
export class MyAnnouncementsService {
    announcementResourceService = inject(AnnouncementResourceService)

    getMyAnnouncements(page: number, size: number, sort: string[]) {
       return this.announcementResourceService.getMyAnnouncements(page, size, sort);
    }
}

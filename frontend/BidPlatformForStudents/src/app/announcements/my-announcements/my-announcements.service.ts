import {inject, Injectable} from "@angular/core";
import {AnnouncementResourceService} from "../resource-service/announcement-resource.service";
import {AnnouncementListFilters} from "../../utils/announcement-list/announcement-list-filters";
import {AnnouncementSortData} from "../../utils/announcement-list/announcement-sort-data";

@Injectable({
    providedIn: 'root'
})
export class MyAnnouncementsService {
    announcementResourceService = inject(AnnouncementResourceService)

    getMyAnnouncements(page: number, size: number, filters: AnnouncementListFilters | undefined, sort?: AnnouncementSortData) {
       return this.announcementResourceService.getMyAnnouncements(page, size, filters, sort);
    }

    delete(id: number) {
      return this.announcementResourceService.delete(id)
    }
}

import {inject, Injectable} from "@angular/core";
import {AnnouncementResourceService} from "../resource-service/announcement-resource.service";

@Injectable({
  providedIn: "root"
})
export class AnnouncementDetailsService {
  announcementResourceService = inject(AnnouncementResourceService)
  getDetails(announcementId: number | null) {
    return this.announcementResourceService.getDetails(announcementId)
  }
}

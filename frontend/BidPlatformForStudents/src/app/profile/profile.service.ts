import {inject, Injectable} from "@angular/core";
import {UserResourceService} from "../auth/resource-service/user-resource.service";
import {UserDtoModel} from "../auth/domain/user-dto.model";
import {AnnouncementResourceService} from "../announcements/resource-service/announcement-resource.service";
import {Observable} from "rxjs";
import {ChartDataVoModel} from "../announcements/domain/chart-data-vo.model";

@Injectable({
  providedIn: "root"
})
export class ProfileService {
  userResourceService = inject(UserResourceService)
  announcementResourceService = inject(AnnouncementResourceService)

  getProfileDetails() {
    return this.userResourceService.getProfileDetails()
  }

  editProfileDetails(userDto: UserDtoModel) {
    return this.userResourceService.editProfileDetails(userDto)
  }

  getChartData(): Observable<ChartDataVoModel> {
    return this.announcementResourceService.getChartData();
  }
}

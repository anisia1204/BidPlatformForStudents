import {inject, Injectable} from "@angular/core";
import {AdminFeaturesResourceService} from "../resource-service/admin-features-resource.service";
import {UpdateUserPointsDtoModel} from "../domain/update-user-points-dto.model";

@Injectable({
  providedIn: "root"
})
export class UpdateUserPointsService {
  adminFeaturesResourceService = inject(AdminFeaturesResourceService)
  getUserDetails(id: string | null) {
    return this.adminFeaturesResourceService.getUserDetails(id)
  }

  updateUserPoints(updateUserPointsDto: UpdateUserPointsDtoModel) {
    return this.adminFeaturesResourceService.updateUserPoints(updateUserPointsDto)
  }
}

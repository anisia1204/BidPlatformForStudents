import {inject, Injectable} from "@angular/core";
import {WithdrawResourceService} from "../resource-service/withdraw-resource.service";
import {UpdateUserPointsDtoModel} from "../domain/update-user-points-dto.model";

@Injectable({
  providedIn: "root"
})
export class UpdateUserPointsService {
  withdrawResourceService = inject(WithdrawResourceService)
  getUserDetails(id: string | null) {
    return this.withdrawResourceService.getUserDetails(id)
  }

  updateUserPoints(updateUserPointsDto: UpdateUserPointsDtoModel) {
    return this.withdrawResourceService.updateUserPoints(updateUserPointsDto)
  }
}

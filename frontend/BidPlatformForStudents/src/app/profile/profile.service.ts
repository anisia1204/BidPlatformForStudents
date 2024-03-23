import {inject, Injectable} from "@angular/core";
import {UserResourceService} from "../auth/resource-service/user-resource.service";
import {UserDtoModel} from "../auth/domain/user-dto.model";

@Injectable({
  providedIn: "root"
})
export class ProfileService {
  userResourceService = inject(UserResourceService)

  getProfileDetails() {
    return this.userResourceService.getProfileDetails()
  }

  editProfileDetails(userDto: UserDtoModel) {
    return this.userResourceService.editProfileDetails(userDto)
  }
}

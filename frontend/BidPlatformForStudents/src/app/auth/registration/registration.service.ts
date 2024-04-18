import {inject, Injectable} from "@angular/core";
import {UserResourceService} from "../resource-service/user-resource.service";
import {UserDtoModel} from "../domain/user-dto.model";

@Injectable({
  providedIn: "root"
})
export class RegistrationService {
  userResourceService = inject(UserResourceService)

  saveUser(userDto: UserDtoModel, files: File[])  {
    return this.userResourceService.saveUser(userDto, files);
  }
}

import {inject, Injectable} from "@angular/core";
import {UserResourceService} from "../resource-service/user-resource.service";
import {UserDtoModel} from "../domain/user-dto.model";

@Injectable({
  providedIn: "root"
})
export class LoginService {
  userResourceService = inject(UserResourceService)

  login(userDto: UserDtoModel) {
    return this.userResourceService.login(userDto);
  }
}

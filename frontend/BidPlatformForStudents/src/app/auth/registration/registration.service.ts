import {inject, Injectable} from "@angular/core";
import {UserResourceService} from "../resource-service/user-resource.service";
import {UserDtoModel} from "../domain/user-dto.model";
import {Observable} from "rxjs";

@Injectable({
  providedIn: "root"
})
export class RegistrationService {
  userResourceService = inject(UserResourceService)

  saveUser(userDto: UserDtoModel) : Observable<UserDtoModel> {
    return this.userResourceService.saveUser(userDto);
  }
}

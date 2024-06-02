import {inject, Injectable} from "@angular/core";
import {UserResourceService} from "../../../auth/resource-service/user-resource.service";

@Injectable({
    providedIn: "root"
})
export class NewChatService {
    userResourceService = inject(UserResourceService)

    getFilteredUserEmails(email: string) {
        return this.userResourceService.getFilteredUserEmails(email)
    }
}

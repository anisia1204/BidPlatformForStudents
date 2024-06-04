import {inject, Injectable} from "@angular/core";
import {ChatRoomResourceService} from "../../chat/resource-service/chat-room-resource.service";

@Injectable({
  providedIn: "root"
})
export class SidebarService {
  chatRoomResourceService = inject(ChatRoomResourceService)

  getCountOfUnreadMessagesOfUser() {
    return this.chatRoomResourceService.getCountOfUnreadMessagesOfUser()
  }
}

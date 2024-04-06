import {inject, Injectable} from "@angular/core";
import {ChatRoomResourceService} from "../../resource-service/chat-room-resource.service";

@Injectable({
  providedIn: "root"
})
export class ChatListService {
  chatRoomResourceService = inject(ChatRoomResourceService)

  findMyChatRooms() {
    return this.chatRoomResourceService.findMyChatRooms()
  }
}

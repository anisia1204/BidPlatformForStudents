import {ChatRoomResourceService} from "../resource-service/chat-room-resource.service";
import {inject, Injectable} from "@angular/core";
@Injectable({
  providedIn: "root"
})
export class ChatPageService {
  chatRoomResourceService = inject(ChatRoomResourceService)

  getChatRoomMessages(recipientId: number | undefined, page?: number) {
    return this.chatRoomResourceService.findChatRoomMessages(recipientId, page)
  }

  findMyChatRooms() {
    return this.chatRoomResourceService.findMyChatRooms()
  }
}

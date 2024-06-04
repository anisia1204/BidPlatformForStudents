import {inject, Injectable} from "@angular/core";
import {ChatRoomResourceService} from "../../resource-service/chat-room-resource.service";
import {ChatRoomListItemVoModel} from "../../domain/chat-room-list-item-vo.model";

@Injectable({
  providedIn: "root"
})
export class ChatListService {
  chatRoomResourceService = inject(ChatRoomResourceService)

  markUnreadChatMessagesOfChatRoomAsRead(chatRoom: ChatRoomListItemVoModel) {
    return this.chatRoomResourceService.markUnreadChatMessagesOfChatRoomAsRead(chatRoom)
  }
}

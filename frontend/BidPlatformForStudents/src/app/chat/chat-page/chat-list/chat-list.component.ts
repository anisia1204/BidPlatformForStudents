import {Component, inject, OnDestroy, OnInit, Output, EventEmitter, Input} from '@angular/core';
import {Subject} from "rxjs";
import {ChatRoomListItemVoModel} from "../../domain/chat-room-list-item-vo.model";
import {ChatRoomStompService} from "../../../utils/chat-room-stomp.service";
import {ChatListService} from "./chat-list.service";

@Component({
  selector: 'app-chat-list',
  templateUrl: './chat-list.component.html',
  styleUrls: ['./chat-list.component.scss']
})
export class ChatListComponent implements OnDestroy{
  destroy$: Subject<boolean> = new Subject<boolean>()
  @Output() chatRoomSelect: EventEmitter<{recipientId: number | undefined, page?: number}> = new EventEmitter<{recipientId: number | undefined, page?: number}>()
  @Input() chats: ChatRoomListItemVoModel[] = []
  selectedChatRoom : number | undefined

  chatListService = inject(ChatListService)


  ngOnDestroy(): void {
    this.destroy$.next(true)
  }

  isSelected(recipientId: number | undefined) {
    return this.selectedChatRoom === recipientId;
  }

  getChatRoomMessages(recipientId: number | undefined) {
    this.selectedChatRoom = recipientId
    const chatRoom = this.chats.find(chat => chat.chatRoomVO?.recipientId === recipientId);
    if (chatRoom && chatRoom.hasUnreadMessages) {
      chatRoom.hasUnreadMessages = false;
      this.chatListService.markUnreadChatMessagesOfChatRoomAsRead(chatRoom)
        .subscribe()
    }
    this.chatRoomSelect.emit({recipientId: recipientId})
  }
}

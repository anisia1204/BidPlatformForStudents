import {Component, inject, OnDestroy, OnInit, Output, EventEmitter, Input} from '@angular/core';
import {Subject, takeUntil} from "rxjs";
import {ChatRoomListItemVoModel} from "../../domain/chat-room-list-item-vo.model";
import {formatDate} from "@angular/common";
import {ChatRoomStompService} from "../../../utils/chat-room-stomp.service";

@Component({
  selector: 'app-chat-list',
  templateUrl: './chat-list.component.html',
  styleUrls: ['./chat-list.component.scss']
})
export class ChatListComponent implements OnDestroy{

  chatRoomStompService = inject(ChatRoomStompService)

  destroy$: Subject<boolean> = new Subject<boolean>()
  @Output() chatRoomSelect: EventEmitter<{recipientId: number | undefined, page?: number}> = new EventEmitter<{recipientId: number | undefined, page?: number}>()
  @Input() chats: ChatRoomListItemVoModel[] = []
  selectedChatRoom : number | undefined



  ngOnDestroy(): void {
    this.destroy$.next(true)
  }

  isSelected(recipientId: number | undefined) {
    return this.selectedChatRoom === recipientId;
  }

  getChatRoomMessages(recipientId: number | undefined) {
    this.selectedChatRoom = recipientId
    this.chatRoomSelect.emit({recipientId: recipientId})
  }
}

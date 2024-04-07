import {Component, inject, OnDestroy, OnInit, Output, EventEmitter} from '@angular/core';
import {ChatListService} from "./chat-list.service";
import {Subject, takeUntil} from "rxjs";
import {ChatRoomVoModel} from "../../domain/chat-room-vo.model";

@Component({
  selector: 'app-chat-list',
  templateUrl: './chat-list.component.html',
  styleUrls: ['./chat-list.component.scss']
})
export class ChatListComponent implements OnInit, OnDestroy{

  chatListService = inject(ChatListService)
  destroy$: Subject<boolean> = new Subject<boolean>()
  @Output() chatRoomSelect: EventEmitter<{recipientId: number | undefined, page?: number}> = new EventEmitter<{recipientId: number | undefined, page?: number}>()
  chats: ChatRoomVoModel[] = []
  selectedChatRoom : number | undefined

  ngOnInit(): void {
    this.chatListService.findMyChatRooms()
      .pipe(takeUntil(this.destroy$))
      .subscribe(res => {
        this.chats = res;
      })
  }

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

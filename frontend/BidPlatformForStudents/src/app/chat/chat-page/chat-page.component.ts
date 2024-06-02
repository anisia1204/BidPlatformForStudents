import {Component, inject, OnDestroy, OnInit} from '@angular/core';
import {ChatPageService} from "./chat-page.service";
import {ChatMessageVoModel} from "../domain/chat-message-vo.model";
import {Subject, takeUntil} from "rxjs";
import {ChatMessageDtoModel} from "../domain/chat-message-dto.model";
import {ChatRoomStompService} from "../../utils/chat-room-stomp.service";
import {ChatRoomListItemVoModel} from "../domain/chat-room-list-item-vo.model";

@Component({
  selector: 'app-chat-page',
  templateUrl: './chat-page.component.html',
  styleUrls: ['./chat-page.component.scss']
})
export class ChatPageComponent implements OnInit, OnDestroy{
  chatPageService = inject(ChatPageService)
  chatRoomStompService = inject(ChatRoomStompService)
  destroy$: Subject<boolean> = new Subject<boolean>()
  chatRoomMessages: ChatMessageVoModel[] = []
  recipientId: number | undefined
  visible: boolean = false;
  chats: ChatRoomListItemVoModel[] = []
  ngOnInit(): void {
    this.getChatRooms()
  }

  getChatRooms() {
    this.chatPageService.findMyChatRooms()
        .pipe(takeUntil(this.destroy$))
        .subscribe(res => {
          this.chats = res;
        })
  }

  onChatRoomSelect(value: {recipientId: number | undefined, page?: number}) {
    this.chatPageService
      .getChatRoomMessages(value.recipientId, value.page)
      .pipe(takeUntil(this.destroy$))
      .subscribe(res => {
        this.chatRoomMessages = res.reverse()
        if(!value.page) {
          this.recipientId = value.recipientId
          this.chatRoomStompService.recipientId = this.recipientId
        }

      })
  }

  onSubmit(chatMessageDtoModel: ChatMessageDtoModel) {
    this.chatRoomStompService.sendMessage(chatMessageDtoModel)
  }
  ngOnDestroy(): void {
    this.destroy$.next(true)
  }

  onMessageSent(event: boolean) {
    this.visible = !event
    this.getChatRooms()
  }
}

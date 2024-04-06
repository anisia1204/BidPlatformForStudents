import {Component, inject, OnDestroy} from '@angular/core';
import {ChatPageService} from "./chat-page.service";
import {ChatMessageVoModel} from "../domain/chat-message-vo.model";
import {Subject, takeUntil} from "rxjs";
import {ChatMessageDtoModel} from "../domain/chat-message-dto.model";
import {ChatRoomStompService} from "./chat-room-stomp.service";

@Component({
  selector: 'app-chat-page',
  templateUrl: './chat-page.component.html',
  styleUrls: ['./chat-page.component.scss']
})
export class ChatPageComponent implements OnDestroy{
  chatPageService = inject(ChatPageService)
  chatRoomStompService = inject(ChatRoomStompService)
  destroy$: Subject<boolean> = new Subject<boolean>()
  chatRoomMessages: ChatMessageVoModel[] = []
  showChatArea = false;
  recipientId: number | undefined
  onChatRoomSelect(recipientId: number) {
    this.chatPageService
      .getChatRoomMessages(recipientId)
      .pipe(takeUntil(this.destroy$))
      .subscribe(res => {
        this.chatRoomMessages = res
        this.showChatArea = true
        this.recipientId = recipientId
        this.chatRoomStompService.recipientId = this.recipientId
        console.log(this.chatRoomStompService.recipientId)
        this.chatRoomStompService.connectToChat()
      })
  }

  onSubmit(chatMessageDtoModel: ChatMessageDtoModel) {
    this.chatRoomStompService.sendMessage(chatMessageDtoModel)
  }
  ngOnDestroy(): void {
    this.destroy$.next(true)
  }
}

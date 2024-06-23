import {Component, EventEmitter, inject, Input, OnDestroy, Output} from '@angular/core';
import {AutoCompleteCompleteEvent} from "primeng/autocomplete";
import {NewChatService} from "./new-chat.service";
import {Subject, takeUntil} from "rxjs";
import {FormBuilder, Validators} from "@angular/forms";
import {ChatMessageDtoModel} from "../../domain/chat-message-dto.model";
import {ChatRoomStompService} from "../../../utils/chat-room-stomp.service";

@Component({
  selector: 'app-new-chat',
  templateUrl: './new-chat.component.html',
  styleUrls: ['./new-chat.component.css']
})
export class NewChatComponent implements OnDestroy{
  @Input() visible: boolean = false;
  @Output() hide = new EventEmitter<boolean>()
  @Output() messageSent = new EventEmitter<boolean>()
  selectedItem: any;
  suggestions: any;
  form: any
  newChatService = inject(NewChatService)
  formBuilder = inject(FormBuilder)
  chatRoomStompService = inject(ChatRoomStompService)
  private destroy$ = new Subject<boolean>();

  constructor() {
    this.form = this.formBuilder.group({
      recipient: [null, Validators.required],
      message: [null, Validators.required]
    })
  }
  search(event: AutoCompleteCompleteEvent) {
    this.newChatService.getFilteredUserEmails(event.query)
        .pipe(takeUntil(this.destroy$))
        .subscribe(res => this.suggestions = res)
  }

  ngOnDestroy() {
    this.chatRoomStompService.disconnect()
    this.destroy$.next(true)
  }

  onSubmit() {
    if(this.form.value) {
      const chatMessageDto = new ChatMessageDtoModel()
      chatMessageDto.content = this.form.value.message
      chatMessageDto.recipientId = this.form.value.recipient.id
      this.form.reset()
      this.chatRoomStompService.sendMessage(chatMessageDto)
      this.messageSent.emit(true)
    }
  }

  onHide() {
    this.form.reset()
    this.hide.emit(true)
  }
}

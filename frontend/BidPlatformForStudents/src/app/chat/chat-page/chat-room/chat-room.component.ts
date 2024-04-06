import {Component, EventEmitter, inject, Input, Output} from '@angular/core';
import {ChatMessageVoModel} from "../../domain/chat-message-vo.model";
import {FormBuilder, FormGroup, NgForm} from "@angular/forms";
import {ChatMessageDtoModel} from "../../domain/chat-message-dto.model";

@Component({
  selector: 'app-chat-room',
  templateUrl: './chat-room.component.html',
  styleUrls: ['./chat-room.component.scss']
})
export class ChatRoomComponent {
  @Input() chatMessages: ChatMessageVoModel[] | null = []
  @Input() showChatArea = false
  @Input() recipientId : number | undefined
  @Output() submit: EventEmitter<ChatMessageDtoModel> = new EventEmitter<ChatMessageDtoModel>()
  form: FormGroup | any;
  formBuilder = inject(FormBuilder)
  constructor() {
    this.form = this.formBuilder.group({
      message: [null]
    })
  }

  onSubmit() {
    const chatMessageDto = new ChatMessageDtoModel()
    chatMessageDto.content = this.form.value.message
    chatMessageDto.recipientId = this.recipientId
    this.submit.emit(chatMessageDto)
  }
}

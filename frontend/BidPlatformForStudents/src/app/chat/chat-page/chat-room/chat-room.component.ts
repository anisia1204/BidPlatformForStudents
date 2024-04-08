import {
    AfterViewInit,
    Component,
    ElementRef,
    EventEmitter,
    inject,
    Input,
    OnDestroy,
    OnInit,
    Output,
    ViewChild
} from '@angular/core';
import {ChatMessageVoModel} from "../../domain/chat-message-vo.model";
import {FormBuilder, FormGroup} from "@angular/forms";
import {ChatMessageDtoModel} from "../../domain/chat-message-dto.model";
import {ChatRoomStompService} from "../../../utils/chat-room-stomp.service";
import {Subject, takeUntil} from "rxjs";

@Component({
  selector: 'app-chat-room',
  templateUrl: './chat-room.component.html',
  styleUrls: ['./chat-room.component.scss']
})
export class ChatRoomComponent implements OnInit, OnDestroy, AfterViewInit {
  _chatMessages: ChatMessageVoModel[] | null = []
  private element: any;
  @Input() set chatMessages(msgs: ChatMessageVoModel[]) {
    this._chatMessages?.unshift(...msgs)
  }
  get chatMessages() {
    return this._chatMessages!
  }

  _recipientId : number | undefined
  @Input() set recipientId(value: number | undefined) {
    this._recipientId = value
  }
  get recipientId() {
    return this._recipientId
  }

  @Output() submit: EventEmitter<ChatMessageDtoModel> = new EventEmitter<ChatMessageDtoModel>()
  @Output() loadMessages = new EventEmitter<{recipientId: number | undefined, page: number}>()
  form: FormGroup | any;
  formBuilder = inject(FormBuilder)
  chatRoomStompService = inject(ChatRoomStompService)
  destroy$ = new Subject<boolean>()
  @ViewChild('chatArea') chatArea!: ElementRef;

  constructor() {
    this.form = this.formBuilder.group({
      message: [null]
    })
  }
  ngOnInit(): void {
    this.chatRoomStompService.receivedMessage$
      .pipe(takeUntil(this.destroy$))
      .subscribe(res => {
        if(res) {
          this.chatMessages?.push(res)
        }
      })
  }

  ngAfterViewInit(): void {
     this.scrollToBottom();
  }
  onSubmit() {
    const chatMessageDto = new ChatMessageDtoModel()
    chatMessageDto.content = this.form.value.message
    chatMessageDto.recipientId = this.recipientId
    this.form.reset()
    this.submit.emit(chatMessageDto)
  }

  page = 1
  onScroll(): void {
    const element = this.chatArea.nativeElement;
    const atTop = element.scrollTop === 0;
    if (atTop) {
      this.loadMessages.emit({recipientId: this.recipientId, page: this.page++})
    }
  }

  scrollToBottom(): void {
    this.element = this.chatArea.nativeElement;
    this.element.scrollTop = this.element.scrollHeight;
  }

  ngOnDestroy(): void {
    this.destroy$.next(true)
  }
}

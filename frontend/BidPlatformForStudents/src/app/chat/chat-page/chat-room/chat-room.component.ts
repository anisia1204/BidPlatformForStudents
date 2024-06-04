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
import {LazyLoadEvent} from "primeng/api";

@Component({
  selector: 'app-chat-room',
  templateUrl: './chat-room.component.html',
  styleUrls: ['./chat-room.component.scss']
})
export class ChatRoomComponent implements OnInit, OnDestroy, AfterViewInit{
  _chatMessages: ChatMessageVoModel[] | null = []
  _page: number | undefined
  private element: any;
  page: number = 0;
  @Input() set chatMessages(msgs: ChatMessageVoModel[]) {
    if (msgs.length) {
      this._chatMessages = msgs;
    }
  }

  get chatMessages() {
    return this._chatMessages!
  }

  _recipientId : number | undefined
  @Input() set recipientId(value: number | undefined) {
    this._recipientId = value
    this.page = 0
    this.scrollToBottom();
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
  ngAfterViewInit() {
    this.scrollToBottom()
  }

  onSubmit() {
    const chatMessageDto = new ChatMessageDtoModel()
    chatMessageDto.content = this.form.value.message
    chatMessageDto.recipientId = this.recipientId
    this.form.reset()
    this.scrollToBottom()
    this.submit.emit(chatMessageDto)
  }

  onScroll(): void {
    const element = this.chatArea.nativeElement;
    const atTop = element.scrollTop === 0;
    if (atTop) {
      const scrollHeightBefore = element.scrollHeight;
      this.loadMessages.emit({recipientId: this.recipientId, page: this.page++});

      setTimeout(() => {
        const scrollHeightAfter = element.scrollHeight;
        element.scrollTop = scrollHeightAfter - scrollHeightBefore;
      }, 100);
    }
  }
  scrollToBottom(): void {
    this.element = this.chatArea.nativeElement;
    this.element.scrollTop = this.element.scrollHeight;
  }

  showDateHeader(index: number): boolean {
    if (index === 0) return true;
    const currentMessageDate = this.getMessageDate(this.chatMessages[index]);
    const previousMessageDate = this.getMessageDate(this.chatMessages[index - 1]);
    return currentMessageDate !== previousMessageDate;
  }

  getMessageDate(message: ChatMessageVoModel): string {
    if (!message.timestamp) return '';
    const date = new Date(message.timestamp);
    return date.toLocaleDateString();
  }

  ngOnDestroy(): void {
    this.destroy$.next(true)
  }
}

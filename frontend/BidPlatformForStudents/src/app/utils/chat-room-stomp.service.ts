import * as Stomp from 'stompjs';
import * as SockJS from 'sockjs-client';
import {inject, Injectable, Input} from "@angular/core";
import {BehaviorSubject} from "rxjs";
import {UserContextService} from "../auth/user-context-service/user-context.service";
import {ChatMessageDtoModel} from "../chat/domain/chat-message-dto.model";
import {ChatMessageVoModel} from "../chat/domain/chat-message-vo.model";
@Injectable({
  providedIn: "root"
})
export class ChatRoomStompService {
  url = 'http://localhost:8081/ws';
  socket?: WebSocket;
  stompClient?: Stomp.Client;
  userContextService = inject(UserContextService)
  id: number | undefined
  _recipientId: number | undefined
  @Input() set recipientId(id: number | undefined) {
    this._recipientId = id
  }
  get recipientId() : number | undefined{
    return this._recipientId
  }

  private receivedMessageSubject = new BehaviorSubject<ChatMessageVoModel | null>(null)
  receivedMessage$ = this.receivedMessageSubject.asObservable()
  connectToChat(): void {
    this.userContextService.getLoggedInUser().subscribe(res => {
      this.id = res?.id;
    });
    this.initWebSocket();

  }
  initWebSocket() {
    console.log('connecting to chat...');
    this.socket = new SockJS(this.url);
    this.stompClient = Stomp.over(this.socket);

    this.stompClient.connect({}, (frame) => {
      console.log('connected to: ' + frame);
      this.stompClient!.subscribe(
        `/user/${this.id}/queue/messages`,
        this.onMessageReceived
      );
    });
  }
  onMessageReceived = (payload: any) => {
    const message = JSON.parse(payload.body);
    this.receivedMessageSubject.next(message)
    // if (this.recipientId && this.recipientId === message.senderId) {
    //
    // }
  }


  sendMessage(chatMessageDto: ChatMessageDtoModel) {
    if(this.stompClient) {
      chatMessageDto.senderId = this.id
      this.stompClient.send('/app/chat', {}, JSON.stringify(chatMessageDto))
      this.receivedMessageSubject.next(chatMessageDto)
    }
  }
}

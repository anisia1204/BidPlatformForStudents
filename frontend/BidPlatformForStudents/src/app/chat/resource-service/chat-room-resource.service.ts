import {Injectable} from "@angular/core";
import {HttpClient, HttpParams} from "@angular/common/http";
import {ChatRoomVoModel} from "../domain/chat-room-vo.model";
import {ChatMessageVoModel} from "../domain/chat-message-vo.model";
import {ChatRoomListItemVoModel} from "../domain/chat-room-list-item-vo.model";
import {map} from "rxjs";

@Injectable({
  providedIn: "root"
})
export class ChatRoomResourceService {
  url = "http://localhost:8081/api/chat"
  constructor(private httpClient: HttpClient) {
  }
  findMyChatRooms() {
    return this.httpClient.get<ChatRoomListItemVoModel[]>(`${this.url}/chat-rooms`)
  }

  findChatRoomMessages(recipientId: number | undefined, page? : number) {
    let params: HttpParams = new HttpParams()
    if(page) {
        params = new HttpParams()
            .set('page', page.toString())
    }
    return this.httpClient.get<ChatMessageVoModel[]>(`${this.url}/messages/${recipientId}`, {params})
  }

  markUnreadChatMessagesOfChatRoomAsRead(chatRoom: ChatRoomListItemVoModel) {
    return this.httpClient.patch<boolean>(`${this.url}/unread-messages`, chatRoom)
  }

  getCountOfUnreadMessagesOfUser() {
    return this.httpClient.get<number>(`${this.url}/unread-messages`)
  }
}

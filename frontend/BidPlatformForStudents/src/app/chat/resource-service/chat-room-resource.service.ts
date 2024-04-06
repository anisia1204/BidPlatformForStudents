import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {ChatRoomVoModel} from "../domain/chat-room-vo.model";
import {ChatMessageVoModel} from "../domain/chat-message-vo.model";

@Injectable({
  providedIn: "root"
})
export class ChatRoomResourceService {
  url = "http://localhost:8081/api/chat"
  constructor(private httpClient: HttpClient) {
  }
  findMyChatRooms() {
    return this.httpClient.get<ChatRoomVoModel[]>(`${this.url}/chat-rooms`)
  }

  findChatRoomMessages(recipientId: number | undefined) {
    return this.httpClient.get<ChatMessageVoModel[]>(`${this.url}/messages/${recipientId}`)
  }
}

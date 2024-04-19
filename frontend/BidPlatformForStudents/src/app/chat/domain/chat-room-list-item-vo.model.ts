import {ChatRoomVoModel} from "./chat-room-vo.model";
import {ChatMessageVoModel} from "./chat-message-vo.model";

export class ChatRoomListItemVoModel {
  chatRoomVO: ChatRoomVoModel | null = null;
  lastMessage: ChatMessageVoModel | null = null;
}

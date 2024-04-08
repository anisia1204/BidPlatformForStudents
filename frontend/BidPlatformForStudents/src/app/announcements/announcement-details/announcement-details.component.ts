import {Component, inject, OnDestroy, OnInit} from '@angular/core';
import {AnnouncementDetailsService} from "./announcement-details.service";
import {AnnouncementVoModel} from "../domain/announcement-vo.model";
import {ActivatedRoute} from "@angular/router";
import {Subject, takeUntil} from "rxjs";
import {ChatRoomStompService} from "../../utils/chat-room-stomp.service";
import {FormBuilder} from "@angular/forms";
import {ChatMessageDtoModel} from "../../chat/domain/chat-message-dto.model";

@Component({
  selector: 'app-announcement-details',
  templateUrl: './announcement-details.component.html',
  styleUrls: ['./announcement-details.component.scss']
})
export class AnnouncementDetailsComponent implements OnInit, OnDestroy{
  announcementDetailsService = inject(AnnouncementDetailsService)
  route = inject(ActivatedRoute)
  chatRoomStompService = inject(ChatRoomStompService)
  fb = inject(FormBuilder)
  form: any
  announcement: AnnouncementVoModel | null = null;
  announcementType: string | null = null;
  myAnnouncements: boolean = false;
  id: number | null = null
  destroy$: Subject<boolean> = new Subject<boolean>()

  constructor() {
    this.form = this.fb.group({
      message: [null]
    })
  }

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      this.announcementType = history.state.type
      this.id = history.state.id
      this.myAnnouncements = history.state.myAnnouncements
      this.announcementDetailsService
        .getDetails(this.id)
        .pipe(takeUntil(this.destroy$))
        .subscribe(announcementVO => this.announcement = announcementVO)
    })
  }

  onSubmit(id: number | undefined) {
    const chatMessageDto = new ChatMessageDtoModel()
    chatMessageDto.content = this.form.value.message
    chatMessageDto.recipientId = id
    this.form.reset()
    this.chatRoomStompService.sendMessage(chatMessageDto)
  }

  ngOnDestroy(): void {
    this.destroy$.next(true)
  }

  isMessageInput = false;
  showMessageInput() {
    this.isMessageInput = true
  }
}

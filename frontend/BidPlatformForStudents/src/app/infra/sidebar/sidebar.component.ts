import {Component, inject, OnDestroy, OnInit} from '@angular/core';
import {Subject, takeUntil} from "rxjs";
import {SidebarService} from "./sidebar-service";

@Component({
  selector: 'app-sidebar',
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.css']
})
export class SidebarComponent implements OnInit, OnDestroy {
  destroy$ = new Subject<boolean>
  sidebarVisible: boolean = false;
  messageCounter = 0
  sidebarService = inject(SidebarService)
  ngOnInit() {
    this.getCountOfUnreadMessagesOfUser()
  }

  getCountOfUnreadMessagesOfUser() {
    this.sidebarService.getCountOfUnreadMessagesOfUser()
      .pipe(takeUntil(this.destroy$))
      .subscribe(res => {
        this.messageCounter = res
      })
  }

  openSidebar() {
    this.getCountOfUnreadMessagesOfUser()
    this.sidebarVisible = true
  }
  ngOnDestroy() {
    this.destroy$.next(true)
  }
}

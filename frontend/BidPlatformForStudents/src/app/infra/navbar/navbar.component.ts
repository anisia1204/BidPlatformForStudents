import {Component, inject, OnDestroy, OnInit} from '@angular/core';
import {NavbarService} from "./navbar.service";
import {MenuItem, MessageService} from "primeng/api";
import {Router} from "@angular/router";
import {Subject, Subscription, takeUntil} from "rxjs";
import {DialogService, DynamicDialogRef} from "primeng/dynamicdialog";
import {NewAnnouncementComponent} from "../../announcements/new-announcement/new-announcement.component";

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss'],
  providers: [DialogService, MessageService]
})
export class NavbarComponent implements OnInit, OnDestroy{
  isLoggedIn: boolean = false;
  points : number | undefined
  navbarService = inject(NavbarService)
  router = inject(Router)
  items: MenuItem[] | undefined
  private userSubscription: Subscription | undefined;
  newAnnouncementDialog: DynamicDialogRef | undefined;
  destroy$: Subject<boolean> = new Subject<boolean>()

  constructor(public dialogService: DialogService, private messageService: MessageService) {}

  ngOnInit(): void {
    this.userSubscription = this.navbarService.isLoggedIn().subscribe(
      loggedInUserDto => {
        console.log(loggedInUserDto)
        if(loggedInUserDto) {
          this.isLoggedIn = true
          this.points = loggedInUserDto.points
          this.items = [
            {
              label: "Anunturile mele",
              icon: "pi pi-list",
              routerLink: ['/my-announcements']
            },
            {
              label: "Tranzactiile mele",
              icon: "pi pi-folder",
              routerLink: ['/my-transactions']
            },
            {
              label: "Anunturi favorite",
              icon: "pi pi-heart",
              routerLink: ['/favorites']
            },
          ]
        }
        else {
          this.isLoggedIn = false
        }
      }
    )
  }

  logout() {
    this.navbarService.logout()
  }

  goToMyProfile() {
    this.router.navigate(['/profile'])
  }

  show() {
    this.newAnnouncementDialog = this.dialogService.open(NewAnnouncementComponent, {
      position: "center",
      modal: true,
    });
    this.newAnnouncementDialog.onClose
      .pipe(takeUntil(this.destroy$))
      .subscribe((data: boolean) => {
        if(data){
          this.newAnnouncementDialog?.close()
          this.messageService.add({
            severity: 'info',
            summary: 'Success',
            detail: 'Anuntul a fost creat cu succes!'
          })
        }
      })
  }
  ngOnDestroy(): void {
    this.newAnnouncementDialog?.close()
    this.userSubscription?.unsubscribe()
    this.destroy$.next(true)
  }
}

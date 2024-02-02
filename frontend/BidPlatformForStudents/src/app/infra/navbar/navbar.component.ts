import {Component, inject, OnDestroy, OnInit} from '@angular/core';
import {NavbarService} from "./navbar.service";
import {MenuItem} from "primeng/api";
import {Router} from "@angular/router";
import {Subscription} from "rxjs";
import {DialogService, DynamicDialogRef} from "primeng/dynamicdialog";
import {NewAnnouncementComponent} from "../../announcements/new-announcement/new-announcement.component";

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss'],
  providers: [DialogService]
})
export class NavbarComponent implements OnInit, OnDestroy{
  isLoggedIn: boolean = false;
  points : number | undefined
  navbarService = inject(NavbarService)
  router = inject(Router)
  items: MenuItem[] | undefined
  private userSubscription: Subscription | undefined;
  newAnnouncementDialog: DynamicDialogRef | undefined;
  constructor(public dialogService: DialogService) {}

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
              routerLink: ['/favourites']
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

  ngOnDestroy(): void {
    this.userSubscription?.unsubscribe()
  }

  show() {
    this.newAnnouncementDialog = this.dialogService.open(NewAnnouncementComponent, {
      width: "90%",
      height: "100%",
      position: "center",
      modal: true,
    });
  }

}

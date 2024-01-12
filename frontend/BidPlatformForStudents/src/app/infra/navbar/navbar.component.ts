import {Component, inject, OnInit} from '@angular/core';
import {MenuItem} from "primeng/api";
import {NavbarService} from "./navbar.service";

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss']
})
export class NavbarComponent implements OnInit{
  items: MenuItem[] | any;
  isLoggedIn: boolean = false;
  navbarService = inject(NavbarService)

  ngOnInit(): void {
    this.items = [
      {
        label: 'Logout',
        icon: 'pi pi-fw pi-power-off',
      },{
        label: 'Logout',
        icon: 'pi pi-fw pi-power-off'
      },{
        label: 'Logout',
        icon: 'pi pi-fw pi-power-off'
      }
    ]
  }

  logout() {

  }
}

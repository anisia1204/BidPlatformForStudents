import {Component, OnInit} from '@angular/core';
import {MenuItem} from "primeng/api";

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss']
})
export class NavbarComponent implements OnInit{
  items: MenuItem[] | any;

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

}

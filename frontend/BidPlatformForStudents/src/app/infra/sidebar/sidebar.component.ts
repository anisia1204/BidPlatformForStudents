import {Component, ViewChild} from '@angular/core';
import {Sidebar} from "primeng/sidebar";

@Component({
  selector: 'app-sidebar',
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.scss']
})
export class SidebarComponent {
  sidebarVisible: boolean = false;
}

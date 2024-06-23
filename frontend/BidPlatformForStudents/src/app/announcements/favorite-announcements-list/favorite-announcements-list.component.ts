import { Component } from '@angular/core';
import {cols} from "../../utils/announcement-list/announcement-list-filter-columns";

@Component({
  selector: 'app-favorite-announcements-list',
  templateUrl: './favorite-announcements-list.component.html',
  styleUrls: ['./favorite-announcements-list.component.css']
})
export class FavoriteAnnouncementsListComponent {
  cols = cols
}

import {Component, EventEmitter, Input, Output} from '@angular/core';
import {AnnouncementSortData} from "../../../utils/announcement-list/announcement-sort-data";
import {AnnouncementColumn} from "../../../utils/announcement-list/announcement-column";

@Component({
  selector: 'app-announcement-list-sort',
  templateUrl: './announcement-list-sort.component.html',
  styleUrls: ['./announcement-list-sort.component.scss']
})
export class AnnouncementListSortComponent {
  @Input() column: AnnouncementColumn | null = null
  @Input() sort: AnnouncementSortData | undefined

  @Output() sortChanged = new EventEmitter<AnnouncementSortData>()
  selectedOption: {label?: string, value?: string} | null = null
  onSortChange(option: any, colDef: string) {
    this.selectedOption = option
    if(option) {
      if(this.sort) {
        this.sort.sortField = colDef
        this.sort.sortOrder = option.value
        console.log(this.sort)
      }
    }
    else {
      if(this.sort) {
        this.sort.sortField = null
        this.sort.sortOrder = null
      }
    }
    this.sortChanged.emit(this.sort)
  }
}

import {Component, EventEmitter, Input, Output} from '@angular/core';
import {DropdownChangeEvent} from "primeng/dropdown";
import {AnnouncementListFilters} from "../../../utils/announcement-list/announcement-list-filters";
import {AnnouncementStatusModel} from "../../domain/announcement-status.model";
import {AnnouncementColumn} from "../../../utils/announcement-list/announcement-column";

@Component({
  selector: 'app-announcement-list-filter',
  templateUrl: './announcement-list-filter.component.html',
  styleUrls: ['./announcement-list-filter.component.scss']
})
export class AnnouncementListFilterComponent {
  @Input() column: AnnouncementColumn | null = null
  @Input() options: {label: string, value: string | null | AnnouncementStatusModel}[] = [];
  @Input() filters: AnnouncementListFilters = new AnnouncementListFilters()
  @Input() placeholder: string | undefined = ''
  @Output() filterChanged = new EventEmitter<{filters: AnnouncementListFilters}>()

  dateModel(colDef: string) {
    return this.getFilterValue(this.filters[colDef as keyof AnnouncementListFilters])
  }

  onFilterDropdown(event: DropdownChangeEvent, colDef: string) {
    if(event.value) {
      this.filters = {
        ...(this.filters),
        [colDef]: {
          value: event.value.value,
        }
      }
    }
    else {
      this.filters = {
        ...(this.filters),
        [colDef]:{}
      }
    }
    this.filterChanged.emit({filters: this.filters})
  }

  onFilter(event: Event, colDef: string) {
    let filterValue : any
    if(colDef === 'to' || colDef === 'from') {
      filterValue = new Date(event as any)
    }
    else {
      filterValue = (event.target as HTMLInputElement).value
    }

    if(filterValue) {
      this.filters = {
        ...(this.filters),
        [colDef]: {
          value: filterValue,
        }
      }
    }
    else {
      this.filters = {
        ...(this.filters),
        [colDef]:{}
      }
    }
    this.filterChanged.emit({filters: this.filters})
  }

  getFilterValue = (filter: any) => {
    return filter && 'value' in filter ? filter.value : null;
  };
}

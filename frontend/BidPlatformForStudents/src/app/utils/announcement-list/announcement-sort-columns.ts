import {AnnouncementColumn} from "./announcement-column";
import {sortingTypes} from "./announcement-list-dropdown-options";

export const sortColumns: AnnouncementColumn[] = [
  {
    colDef: 'status',
    type: 'dropdown',
    placeholder: 'Sorteaza dupa status',
    options: sortingTypes
  },
  {
    colDef: 'createdAt',
    type: 'date',
    placeholder: 'Sorteaza dupa data crearii',
    options: sortingTypes
  },
  {
    colDef: 'points',
    type: 'number',
    placeholder: 'Sorteaza dupa puncte',
    options: sortingTypes
  },
]

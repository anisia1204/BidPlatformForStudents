import {AnnouncementColumn} from "./announcement-column";
import {announcementTypes, statusTypes} from "./announcement-list-dropdown-options";

export const cols: AnnouncementColumn[] = [
  {
    colDef: 'announcementTitle',
    type: 'text',
    placeholder: 'Introdu cuvinte cheie'
  },
  {
    colDef: 'announcementType',
    type: 'dropdown',
    options: announcementTypes
  },
  {
    colDef: 'status',
    type: 'dropdown',
    options: statusTypes
  },
  {
    colDef: 'from',
    type: 'date',
    placeholder: 'Create de la'
  },
  {
    colDef: 'to',
    type: 'date',
    placeholder: 'Create pana la'
  },
  {
    colDef: 'min',
    type: 'number',
    placeholder: 'Puncte minime'
  },
  {
    colDef: 'max',
    type: 'number',
    placeholder: 'Puncte maxime'
  }
]

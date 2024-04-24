import {AnnouncementStatusModel} from "../../announcements/domain/announcement-status.model";

export interface AnnouncementColumn {
  colDef: string
  type: string
  options?: {label: string, value: string | null | AnnouncementStatusModel}[]
  placeholder?: string
}

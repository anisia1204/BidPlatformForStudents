import {AnnouncementStatusModel} from "../../announcements/domain/announcement-status.model";

export const announcementTypes = [
  {
    label: "Toate anunturile",
    value: null
  },
  {
    label: "Materiale didactice",
    value: "TeachingMaterial"
  },
  {
    label: "Servicii de tutorat",
    value: "TutoringService"
  },
  {
    label: "Proiecte",
    value: "Project"
  }
]

export const sortingTypes = [
  {
    label: "Crescator",
    value: 'asc'
  },
  {
    label: "Descrescator",
    value: 'desc'
  }
]

export const statusTypes = [
  {
    label: "Toate statusurile",
    value: null
  },
  {
    label: "Active",
    value: AnnouncementStatusModel.ACTIVE
  },
  {
    label: "Vandute",
    value: AnnouncementStatusModel.SOLD
  },
  {
    label: "Inactive",
    value: AnnouncementStatusModel.INACTIVE
  }
]
export const notMyAnnouncementsStatusTypes = [
  {
    label: "Toate statusurile",
    value: null
  },
  {
    label: "Active",
    value: AnnouncementStatusModel.ACTIVE
  },
  {
    label: "Vandute",
    value: AnnouncementStatusModel.SOLD
  }
]

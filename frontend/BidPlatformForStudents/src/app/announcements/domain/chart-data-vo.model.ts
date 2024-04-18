import {CreatedAnnouncementsVoModel} from "./created-announcements-vo.model";
import {SoldAnnouncementsVoModel} from "./sold-announcements-vo.model";

export class ChartDataVoModel {
  createdAnnouncementsVO: CreatedAnnouncementsVoModel | null = null
  soldAnnouncementsVO: SoldAnnouncementsVoModel | null = null
}

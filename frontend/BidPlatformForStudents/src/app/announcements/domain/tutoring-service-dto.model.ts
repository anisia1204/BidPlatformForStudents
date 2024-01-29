import {AnnouncementStatusModel} from "./announcement-status.model";
import {TutoringTypeModel} from "./tutoring-type.model";

export class TutoringServiceDtoModel {
  id: number | undefined;
  userId: number | undefined;
  title: string | undefined;
  description: string | undefined;
  points: number | undefined;
  status: AnnouncementStatusModel | undefined;
  createdAt: string | undefined;
  subject: string | undefined;
  startDate: string | undefined;
  endDate: string | undefined;
  hoursPerSession: number | undefined;
  tutoringType: TutoringTypeModel | undefined;
}

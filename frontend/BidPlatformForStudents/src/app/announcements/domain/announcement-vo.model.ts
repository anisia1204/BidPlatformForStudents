import {AnnouncementStatusModel} from "./announcement-status.model";
import {TutoringTypeModel} from "./tutoring-type.model";
import {AttachmentVoModel} from "./attachment-vo.model";
import {SkillVoModel} from "./skill-vo.model";

export class AnnouncementVoModel {
  id: number | undefined;
  userId: number | undefined;
  title: string | undefined;
  description: string | undefined;
  points: number | undefined;
  status: AnnouncementStatusModel | undefined;
  createdAt: string | undefined;
  domain: string | undefined;
  teamSize: number | undefined;
  requiredSkills: SkillVoModel[] | undefined;
  announcementType: string | undefined;
  name: string | undefined;
  author: string | undefined;
  edition: number | undefined;
  attachmentVOs: string | undefined;
  subject: string | undefined;
  startDate: string | undefined;
  endDate: string | undefined;
  hoursPerSession: number | undefined;
  tutoringType: TutoringTypeModel | undefined;
}

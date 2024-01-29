import {AnnouncementStatusModel} from "./announcement-status.model";
import {SkillDtoModel} from "./skill-dto.model";

export class ProjectDtoModel {
  id: number | undefined;
  userId: number | undefined;
  title: string | undefined;
  description: string | undefined;
  points: number | undefined;
  status: AnnouncementStatusModel | undefined;
  createdAt: string | undefined;
  domain: string | undefined;
  teamSize: number | undefined;
  requiredSkills: SkillDtoModel[] | undefined;
}

import {SkillStatusModel} from "./skill-status.model";

export class SkillVoModel {
  id: number | undefined;
  skill: string | undefined;
  description: string | undefined;
  skillPoints: number | undefined;
  status: SkillStatusModel | undefined;
}

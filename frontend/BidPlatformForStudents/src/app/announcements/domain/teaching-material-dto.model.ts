import {AnnouncementStatusModel} from "./announcement-status.model";
import {AttachmentDtoModel} from "./attachment-dto.model";

export class TeachingMaterialDtoModel {
  id: number | undefined;
  userId: number | undefined;
  title: string | undefined;
  description: string | undefined;
  points: number | undefined;
  status: AnnouncementStatusModel | undefined;
  createdAt: string | undefined;
  name: string | undefined;
  author: string | undefined;
  edition: number | undefined;
  announcementType: string | undefined;
  attachmentDTOs: AttachmentDtoModel[] | undefined;
}

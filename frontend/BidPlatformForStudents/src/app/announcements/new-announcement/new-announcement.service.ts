import {inject, Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {ProjectDtoModel} from "../domain/project-dto.model";
import {TeachingMaterialResourceService} from "../resource-service/teaching-material-resource.service";
import {TutoringServiceDtoModel} from "../domain/tutoring-service-dto.model";
import {ProjectResourceService} from "../resource-service/project-resource.service";
import {TutoringServiceResourceService} from "../resource-service/tutoring-service-resource.service";
import {TeachingMaterialDtoModel} from "../domain/teaching-material-dto.model";

@Injectable({
  providedIn: "root"
})
export class NewAnnouncementService {
  teachingMaterialResourceService = inject(TeachingMaterialResourceService)
  tutoringServiceResourceService = inject(TutoringServiceResourceService)
  projectResourceService = inject(ProjectResourceService)

  saveProjectDto(projectDto: ProjectDtoModel) {
    return this.projectResourceService.save(projectDto);
  }

  saveTeachingMaterialDto(teachingMaterialDto: TeachingMaterialDtoModel) {
    return this.teachingMaterialResourceService.save(teachingMaterialDto);
  }

  saveTutoringServiceDto(tutoringServiceDto: TutoringServiceDtoModel) {
    return this.tutoringServiceResourceService.save(tutoringServiceDto);
  }
}

import {inject, Injectable} from "@angular/core";
import {ProjectDtoModel} from "../domain/project-dto.model";
import {TeachingMaterialResourceService} from "../resource-service/teaching-material-resource.service";
import {TutoringServiceDtoModel} from "../domain/tutoring-service-dto.model";
import {ProjectResourceService} from "../resource-service/project-resource.service";
import {TutoringServiceResourceService} from "../resource-service/tutoring-service-resource.service";
import {TeachingMaterialDtoModel} from "../domain/teaching-material-dto.model";
import {Observable} from "rxjs";

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

  saveTeachingMaterialDto(teachingMaterialDto: TeachingMaterialDtoModel, files: File[]) {
    return this.teachingMaterialResourceService.save(teachingMaterialDto, files);
  }

  saveTutoringServiceDto(tutoringServiceDto: TutoringServiceDtoModel) {
    return this.tutoringServiceResourceService.save(tutoringServiceDto);
  }

  updateProjectDto(projectDto: ProjectDtoModel) {
    return this.projectResourceService.update(projectDto);
  }

  updateTeachingMaterialDto(teachingMaterialDto: TeachingMaterialDtoModel) {
    return this.teachingMaterialResourceService.update(teachingMaterialDto);
  }

  updateTutoringServiceDto(tutoringServiceDto: TutoringServiceDtoModel) {
    return this.tutoringServiceResourceService.update(tutoringServiceDto);
  }

  getAnnouncementTemplate(id: string, routerState: string) {
    switch(routerState) {
      case 'project':
        return this.getTemplateForProject(id);
      case 'teachingMaterial':
        return this.getTemplateForTeachingMaterial(id);
      case 'tutoringService':
        return this.getTemplateForTutoringService(id);
      default:
        return new Observable<any>();
    }
  }

  getTemplateForProject(id: string) {
    return this.projectResourceService.getTemplate(id);
  }

  getTemplateForTutoringService(id: string) {
    return this.tutoringServiceResourceService.getTemplate(id);
  }

  getTemplateForTeachingMaterial(id: string) {
    return this.teachingMaterialResourceService.getTemplate(id);
  }
}

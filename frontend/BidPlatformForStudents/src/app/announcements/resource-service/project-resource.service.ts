import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {ProjectDtoModel} from "../domain/project-dto.model";
import {SkillVoModel} from "../domain/skill-vo.model";

@Injectable({
  providedIn: "root"
})
export class ProjectResourceService {
  private url = "http://localhost:8081/api/project"

  constructor(private httpClient: HttpClient) {
  }

  save(projectDto: ProjectDtoModel) {
    return this.httpClient.post<ProjectDtoModel>(`${this.url}`, projectDto);
  }

  update(projectDto: ProjectDtoModel) {
    return this.httpClient.put<ProjectDtoModel>(`${this.url}`, projectDto);
  }

  getTemplate(id: string) {
    return this.httpClient.get<ProjectDtoModel>(`${this.url}/template/${id}`);
  }

  getSkillsById(id: number | undefined) {
    return this.httpClient.get<SkillVoModel[]>(`${this.url}/${id}`)
  }
}

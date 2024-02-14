import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {TeachingMaterialDtoModel} from "../domain/teaching-material-dto.model";

@Injectable({
  providedIn: "root"
})
export class TeachingMaterialResourceService {
  private url = "http://localhost:8081/api/teaching-material"

  constructor(private httpClient: HttpClient) {
  }

  save(teachingMaterialDto: TeachingMaterialDtoModel, files: File[]) {
    const formData: FormData = new FormData();
    for (let file of files) {
      formData.append('files', file);
    }
    formData.append('teachingMaterialDTO', new Blob([JSON.stringify(teachingMaterialDto)], {
      type: "application/json"
    }));
    return this.httpClient.post<TeachingMaterialDtoModel>(`${this.url}`, formData);
  }

  update(teachingMaterialDto: TeachingMaterialDtoModel, files: File[]) {
    const formData: FormData = new FormData();
    for (let file of files) {
      formData.append('files', file);
    }
    formData.append('teachingMaterialDTO', new Blob([JSON.stringify(teachingMaterialDto)], {
      type: "application/json"
    }));
    return this.httpClient.put<TeachingMaterialDtoModel>(`${this.url}`, formData);
  }

  getTemplate(id: string) {
    return this.httpClient.get<TeachingMaterialDtoModel>(`${this.url}/template/${id}`);
  }
}

import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {TeachingMaterialDtoModel} from "../domain/teaching-material-dto.model";
import {teachingMaterialUrl} from "../../utils/endpoints";

@Injectable({
  providedIn: "root"
})
export class TeachingMaterialResourceService {
  private url = teachingMaterialUrl

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
    formData.append('teachingMaterialDTOString', new Blob([JSON.stringify(teachingMaterialDto)], {
      type: "application/json"
    }));
    return this.httpClient.put<TeachingMaterialDtoModel>(`${this.url}`, formData);
  }

  getTemplate(id: string) {
    return this.httpClient.get<TeachingMaterialDtoModel>(`${this.url}/template/${id}`);
  }
}

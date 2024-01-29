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

  save(teachingMaterialDto: TeachingMaterialDtoModel) {
    return this.httpClient.post<TeachingMaterialDtoModel>(`${this.url}`, teachingMaterialDto);
  }
}

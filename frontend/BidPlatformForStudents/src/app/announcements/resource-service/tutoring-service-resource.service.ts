import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {TutoringServiceDtoModel} from "../domain/tutoring-service-dto.model";

@Injectable({
  providedIn: "root"
})
export class TutoringServiceResourceService {
  private url = "http://localhost:8081/api/tutoring-service"

  constructor(private httpClient: HttpClient) {
  }

  save(tutoringServiceDto: TutoringServiceDtoModel) {
    return this.httpClient.post<TutoringServiceDtoModel>(`${this.url}`, tutoringServiceDto);
  }
}

import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {UserDetailsVoModel} from "../domain/user-details-vo.model";
import {UpdateUserPointsDtoModel} from "../domain/update-user-points-dto.model";

@Injectable({
  providedIn: "root"
})
export class AdminFeaturesResourceService {
  private url: string = "http://localhost:8081/api/admin-features"
  constructor(private httpClient: HttpClient) {
    this.httpClient = httpClient;
  }

  getUserDetails(id: string | null) {
    return this.httpClient.get<UserDetailsVoModel>(`${this.url}/details/${id}`)
  }

  updateUserPoints(updateUserPointsDto: UpdateUserPointsDtoModel) {
    return this.httpClient.put<UpdateUserPointsDtoModel>(`${this.url}`, updateUserPointsDto)
  }
}

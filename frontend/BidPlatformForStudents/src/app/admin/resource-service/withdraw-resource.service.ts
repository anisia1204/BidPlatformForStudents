import {Injectable} from "@angular/core";
import {HttpClient, HttpParams} from "@angular/common/http";
import {UserDetailsVoModel} from "../domain/user-details-vo.model";
import {UpdateUserPointsDtoModel} from "../domain/update-user-points-dto.model";
import {Page} from "../../announcements/domain/page.model";
import {WithdrawVoModel} from "../domain/withdraw-vo.model";
import {adminFeaturesUrl} from "../../utils/endpoints";

@Injectable({
  providedIn: "root"
})
export class WithdrawResourceService {
  private url = adminFeaturesUrl
  constructor(private httpClient: HttpClient) {
    this.httpClient = httpClient;
  }

  getUserDetails(id: string | null) {
    return this.httpClient.get<UserDetailsVoModel>(`${this.url}/details/${id}`)
  }

  updateUserPoints(updateUserPointsDto: UpdateUserPointsDtoModel) {
    return this.httpClient.put<UpdateUserPointsDtoModel>(`${this.url}`, updateUserPointsDto)
  }

  getAll(page: number, size: number, sortField?: string | string[], sortOrder?: number, filters?: any) {
    let params = new HttpParams()
        .set('page', page.toString())
        .set('size', size.toString());
    if (sortField && sortOrder !== null) {
      if (typeof sortField !== "string") {
        sortField.forEach(s => {
          params = params.append('sortField', s);
        });
      }
      else {
        params = params.set('sortField', sortField);
      }
      params = params.set('sortOrder', sortOrder === 1 ? 'asc' : 'desc');
    }

    if (filters) {
      if (filters.userFullName) {
        params = params.set('userFullName', filters.userFullName);
      }
      if (filters.points) {
        params = params.set('points', filters.points.toString());
      }
      if (filters.createdAt) {
        params = params.set('createdAt', filters.createdAt.toISOString());
      }
    }

    return this.httpClient.get<Page<WithdrawVoModel>>(`${this.url}`, {params})
  }
}

import {Injectable} from "@angular/core";
import {HttpClient, HttpParams} from "@angular/common/http";
import {AnnouncementVoModel} from "../domain/announcement-vo.model";
import {Page} from "../domain/page.model";
import {Observable} from "rxjs";
import {FavoriteAnnouncementDtoModel} from "../domain/favorite-announcement-dto.model";

@Injectable({
    providedIn: "root"
})
export class AnnouncementResourceService {
    private url = "http://localhost:8081/api/announcements"

    constructor(private httpClient: HttpClient) {
    }

    getMyAnnouncements(page: number, size: number, sort: string[]) {
      let params = new HttpParams()
        .set('page', page.toString())
        .set('size', size.toString());
      sort.forEach(s => {
        params = params.append('sort', s);
      });
        return this.httpClient.get<Page<AnnouncementVoModel>>(`${this.url}`, {params})
    }

  delete(id: number) {
    return this.httpClient.delete(`${this.url}/${id}`)
  }

  getDetails(id: number | null) : Observable<AnnouncementVoModel> {
      return this.httpClient.get<AnnouncementVoModel>(`${this.url}/${id}`)
  }

  getDashboardAnnouncements(page: number, size: number, sort: string[]) {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());
    sort.forEach(s => {
      params = params.append('sort', s);
    });
    return this.httpClient.get<Page<AnnouncementVoModel>>(`${this.url}/dashboard`, {params})
  }

  addToFavorites(favoriteAnnouncementDto: FavoriteAnnouncementDtoModel) {
    return this.httpClient.post<FavoriteAnnouncementDtoModel>(`${this.url}/favorite`, favoriteAnnouncementDto)
  }

  removeFromFavorites(favoriteAnnouncementId: number) {
    return this.httpClient.delete<FavoriteAnnouncementDtoModel>(`${this.url}/favorite/${favoriteAnnouncementId}`)
  }
}

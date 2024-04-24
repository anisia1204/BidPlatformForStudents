import {Injectable} from "@angular/core";
import {HttpClient, HttpParams} from "@angular/common/http";
import {AnnouncementVoModel} from "../domain/announcement-vo.model";
import {Page} from "../domain/page.model";
import {Observable} from "rxjs";
import {FavoriteAnnouncementDtoModel} from "../domain/favorite-announcement-dto.model";
import {ChartDataVoModel} from "../domain/chart-data-vo.model";
import {AnnouncementListFilters} from "../../utils/announcement-list/announcement-list-filters";
import {AnnouncementSortData} from "../../utils/announcement-list/announcement-sort-data";

@Injectable({
  providedIn: "root"
})
export class AnnouncementResourceService {
  private url = "http://localhost:8081/api/announcements"

  constructor(private httpClient: HttpClient) {
  }

  getMyAnnouncements(page: number, size: number, filters: AnnouncementListFilters | undefined, sort?: AnnouncementSortData) {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());

    if (filters) {
      params = this.setFilterState(params, filters)
    }

    if (sort && sort?.sortField && sort.sortOrder) {
      params = params.set('sortField', sort.sortField);
      params = params.set('sortOrder', sort.sortOrder);
    }

    return this.httpClient.get<Page<AnnouncementVoModel>>(`${this.url}`, {params})
  }


  delete(id: number) {
    return this.httpClient.delete(`${this.url}/${id}`)
  }

  getDetails(id: number | null): Observable<AnnouncementVoModel> {
    return this.httpClient.get<AnnouncementVoModel>(`${this.url}/${id}`)
  }

  getDashboardAnnouncements(page: number, size: number, filters: AnnouncementListFilters | undefined, sort?: AnnouncementSortData) {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());

    if (sort && sort?.sortField && sort.sortOrder) {
      params = params.set('sortField', sort.sortField);
      params = params.set('sortOrder', sort.sortOrder);
    }

    if (filters) {
      params = this.setFilterState(params, filters)
    }

    return this.httpClient.get<Page<AnnouncementVoModel>>(`${this.url}/dashboard`, {params})
  }

  addToFavorites(favoriteAnnouncementDto: FavoriteAnnouncementDtoModel) {
    return this.httpClient.post<FavoriteAnnouncementDtoModel>(`${this.url}/favorite`, favoriteAnnouncementDto)
  }

  removeFromFavorites(favoriteAnnouncementId: number) {
    return this.httpClient.delete<FavoriteAnnouncementDtoModel>(`${this.url}/favorite/${favoriteAnnouncementId}`)
  }

  getFavoriteAnnouncements(page: number, size: number, filters: AnnouncementListFilters | undefined, sort?: AnnouncementSortData) {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());

    if (filters) {
      params = this.setFilterState(params, filters)
    }

    if (sort && sort?.sortField && sort.sortOrder) {
      params = params.set('sortField', sort.sortField);
      params = params.set('sortOrder', sort.sortOrder);
    }

    return this.httpClient.get<Page<AnnouncementVoModel>>(`${this.url}/favorite`, {params})
  }

  getChartData(): Observable<ChartDataVoModel> {
    return this.httpClient.get<ChartDataVoModel>(`${this.url}/chart-data`)
  }

  setFilterState(params: HttpParams, filters: AnnouncementListFilters): HttpParams {
    if (filters.announcementTitle && filters.announcementTitle.value) {
      params = params.set('announcementTitle', filters.announcementTitle.value);
    }
    if(filters.announcementType && filters.announcementType.value) {
      params = params.set('announcementType', filters.announcementType.value);
    }
    if((filters.status && filters.status.value) || filters.status?.value === 0){
      params = params.set('status', filters.status.value);
    }
    if(filters.from && filters.from.value){
      params = params.set('from', filters.from.value.toISOString());
    }
    if(filters.to && filters.to.value){
      params = params.set('to', filters.to.value.toISOString());
    }
    if (filters.min && filters.min.value) {
      params = params.set('min', filters.min.value);
    }
    if (filters.max && filters.max.value) {
      params = params.set('max', filters.max.value);
    }
    return params;
  }
}

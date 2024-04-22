import {Injectable} from "@angular/core";
import {HttpClient, HttpParams} from "@angular/common/http";
import {TransactionDtoModel} from "../domain/transaction-dto.model";
import {Page} from "../../announcements/domain/page.model";
import {TransactionVoModel} from "../domain/transaction-vo.model";
import {DatePipe} from "@angular/common";

@Injectable({
  providedIn: "root"
})
export class TransactionResourceService {
  private url = "http://localhost:8081/api/transaction"

  constructor(private httpClient: HttpClient) {
  }
  onBuy(transactionDto: TransactionDtoModel | undefined) {
    return this.httpClient.post<TransactionDtoModel>(`${this.url}`, transactionDto)
  }

  onBuyProject(transactionDto: TransactionDtoModel | undefined) {
    return this.httpClient.post<TransactionDtoModel>(`${this.url}/project`, transactionDto)
  }

  getMyTransactions(page: number, size: number, sortField?: string | string[], sortOrder?: number, filters?: any) {
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
      if (filters.id) {
        params = params.set('id', filters.id.toString());
      }
      if (filters.announcementTitle) {
        params = params.set('announcementTitle', filters.announcementTitle);
      }
      if (filters.secondUserFullName) {
        params = params.set('secondUserFullName', filters.secondUserFullName);
      }
      if (filters.skill) {
        params = params.set('skill', filters.skill);
      }
      if (filters.type || filters.type === 0) {
          params = params.set('type', filters.type);
      }
      if (filters.amount) {
        params = params.set('amount', filters.amount.toString());
      }
      if (filters.createdAt) {
        params = params.set('createdAt', filters.createdAt.toISOString());
      }
    }

    return this.httpClient.get<Page<TransactionVoModel>>(`${this.url}`, {params})
  }
}

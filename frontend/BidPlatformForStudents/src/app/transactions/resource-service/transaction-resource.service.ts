import {Injectable} from "@angular/core";
import {HttpClient, HttpParams} from "@angular/common/http";
import {TransactionDtoModel} from "../domain/transaction-dto.model";
import {Page} from "../../announcements/domain/page.model";
import {TransactionVoModel} from "../domain/transaction-vo.model";

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

  getMyTransactions(page: number, size: number, sort: string[]) {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());
    // sort.forEach(s => {
    //   params = params.append('sort', s);
    // });
    return this.httpClient.get<Page<TransactionVoModel>>(`${this.url}`, {params})
  }
}

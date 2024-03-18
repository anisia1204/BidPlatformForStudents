import {inject, Injectable} from "@angular/core";
import {TransactionResourceService} from "../resource-service/transaction-resource.service";

@Injectable({
  providedIn: "root"
})
export class TransactionListService {
  transactionResourceService = inject(TransactionResourceService)

  getMyTransactions(first: number, rows: number, sort: string[]) {
    return this.transactionResourceService.getMyTransactions(first, rows, sort)
  }
}

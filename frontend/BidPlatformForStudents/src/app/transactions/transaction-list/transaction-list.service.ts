import {inject, Injectable} from "@angular/core";
import {TransactionResourceService} from "../resource-service/transaction-resource.service";

@Injectable({
  providedIn: "root"
})
export class TransactionListService {
  transactionResourceService = inject(TransactionResourceService)

  getMyTransactions(page: number, size: number, sortField?: string | string[], sortOrder?: number, filters?: any) {
    return this.transactionResourceService.getMyTransactions(page, size, sortField, sortOrder, filters)
  }
}

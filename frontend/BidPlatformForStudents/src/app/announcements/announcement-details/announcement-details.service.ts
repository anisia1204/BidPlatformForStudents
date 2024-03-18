import {inject, Injectable} from "@angular/core";
import {AnnouncementResourceService} from "../resource-service/announcement-resource.service";
import {TransactionResourceService} from "../../transactions/resource-service/transaction-resource.service";
import {TransactionDtoModel} from "../../transactions/domain/transaction-dto.model";

@Injectable({
  providedIn: "root"
})
export class AnnouncementDetailsService {
  announcementResourceService = inject(AnnouncementResourceService)
  transactionResourceService = inject(TransactionResourceService)
  getDetails(announcementId: number | null) {
    return this.announcementResourceService.getDetails(announcementId)
  }

  onBuy(transactionDto: TransactionDtoModel | undefined) {
    return this.transactionResourceService.onBuy(transactionDto)
  }

  onBuyProject(transactionDto: TransactionDtoModel | undefined) {
    return this.transactionResourceService.onBuyProject(transactionDto)
  }
}

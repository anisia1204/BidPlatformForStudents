import {inject, Injectable} from "@angular/core";
import {AnnouncementResourceService} from "../announcements/resource-service/announcement-resource.service";
import {TransactionResourceService} from "../transactions/resource-service/transaction-resource.service";
import {TransactionDtoModel} from "../transactions/domain/transaction-dto.model";

@Injectable({
  providedIn: "root"
})
export class DashboardService {
  announcementResourceService = inject(AnnouncementResourceService)
  transactionResourceService = inject(TransactionResourceService)

  getDashboardAnnouncements(page: number, size: number, sort: string[]) {
    return this.announcementResourceService.getDashboardAnnouncements(page, size, sort)
  }

  buy(transactionDto: TransactionDtoModel | undefined) {
    return this.transactionResourceService.onBuy(transactionDto)
  }

  onBuyProject(transactionDto: TransactionDtoModel | undefined) {
    return this.transactionResourceService.onBuyProject(transactionDto)
  }
}

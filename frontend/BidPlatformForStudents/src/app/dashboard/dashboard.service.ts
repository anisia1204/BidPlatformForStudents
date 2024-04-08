import {inject, Injectable} from "@angular/core";
import {AnnouncementResourceService} from "../announcements/resource-service/announcement-resource.service";
import {TransactionResourceService} from "../transactions/resource-service/transaction-resource.service";
import {TransactionDtoModel} from "../transactions/domain/transaction-dto.model";
import {FavoriteAnnouncementDtoModel} from "../announcements/domain/favorite-announcement-dto.model";

@Injectable({
  providedIn: "root"
})
export class DashboardService {
  announcementResourceService = inject(AnnouncementResourceService)
  transactionResourceService = inject(TransactionResourceService)

  getDashboardAnnouncements(page: number, size: number, sort: string[]) {
    return this.announcementResourceService.getDashboardAnnouncements(page, size, sort)
  }

  getFavoriteAnnouncements(page: number, size: number, sort: string[]) {
    return this.announcementResourceService.getFavoriteAnnouncements(page, size, sort)
  }

  buy(transactionDto: TransactionDtoModel | undefined) {
    return this.transactionResourceService.onBuy(transactionDto)
  }

  addToFavorites(favoriteAnnouncementDto: FavoriteAnnouncementDtoModel) {
    return this.announcementResourceService.addToFavorites(favoriteAnnouncementDto)
  }

  removeFromFavorites(favoriteAnnouncementId: number) {
    return this.announcementResourceService.removeFromFavorites(favoriteAnnouncementId)
  }
}

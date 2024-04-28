import {WithdrawResourceService} from "../resource-service/withdraw-resource.service";
import {inject, Injectable} from "@angular/core";
@Injectable({
    providedIn: "root"
})
export class WithdrawHistoryService {
    withdrawResourceService = inject(WithdrawResourceService)

    getAll(page: number, size: number, sortField?: string | string[], sortOrder?: number, filters?: any) {
        return this.withdrawResourceService.getAll(page, size, sortField, sortOrder, filters)
    }
}

import {TransactionDtoModel} from "../domain/transaction-dto.model";
import {inject, Injectable} from "@angular/core";
import {TransactionResourceService} from "../resource-service/transaction-resource.service";
import {ProjectResourceService} from "../../announcements/resource-service/project-resource.service";
@Injectable({
  providedIn: "root"
})
export class ProjectTransactionService {
  transactionResourceService = inject(TransactionResourceService)
  projectResourceService = inject(ProjectResourceService)
  onBuyProject(transactionDto: TransactionDtoModel | undefined) {
    return this.transactionResourceService.onBuyProject(transactionDto)
  }

  getSkillsByProjectId(id: number | undefined) {
    return this.projectResourceService.getSkillsById(id);
  }
}

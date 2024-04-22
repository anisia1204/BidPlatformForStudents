import {TransactionTypeModel} from "./transaction-type.model";

export class TransactionDtoModel {
  id: number | undefined;
  userId: number | undefined;
  announcementId: number | undefined;
  skillIds: number[] | undefined;
  type: TransactionTypeModel | undefined;
  amount: number | undefined;
  createdAt: string | undefined;
  secondUserId: number | undefined;
}

import {TransactionTypeModel} from "./transaction-type.model";

export class TransactionVoModel {
  id: number | undefined;
  skill: string | undefined;
  announcementTitle: string | undefined;
  type: TransactionTypeModel | undefined;
  amount: number | undefined;
  createdAt: string | undefined;
  secondUserFullName: string | undefined
}

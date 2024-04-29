import {Component, inject, OnDestroy, OnInit} from '@angular/core';
import {TransactionTypeModel} from "../domain/transaction-type.model";
import {TableLazyLoadEvent} from "primeng/table";
import {Subject, takeUntil} from "rxjs";
import {TransactionListService} from "./transaction-list.service";
import {GoBackService} from "../../utils/go-back.service";

@Component({
  selector: 'app-transaction-list',
  templateUrl: './transaction-list.component.html',
  styleUrls: ['./transaction-list.component.scss']
})
export class TransactionListComponent implements OnInit, OnDestroy{
  transactions!: any[];
  transactionListService = inject(TransactionListService)
  goBackService = inject(GoBackService)
  protected readonly TransactionTypeModel = TransactionTypeModel;
  totalRecords: any;
  lazyLoadEvent: TableLazyLoadEvent = {}
  sort = ['createdAt'];
  transactionTypes = [
    {
      label: 'Castig',
      code: TransactionTypeModel.EARN
    },
    {
      label: 'Cheltuire',
      code: TransactionTypeModel.SPEND
    }
  ]

  destroy$: Subject<boolean> = new Subject<boolean>()

  ngOnInit(): void {
    this.goBackService
      .getNavData()
      .pipe(takeUntil(this.destroy$))
      .subscribe(() => {
        this.onLazyLoad(this.lazyLoadEvent)
      })
  }
  onLazyLoad(event: TableLazyLoadEvent) {
    this.lazyLoadEvent  = event ? event : {}

    const filters = {
      id: this.getFilterValue(event.filters?.['id']),
      announcementTitle: this.getFilterValue(event.filters?.['announcementTitle']),
      secondUserFullName: this.getFilterValue(event.filters?.['secondUserFullName']),
      skill: this.getFilterValue(event.filters?.['skill']),
      amount: this.getFilterValue(event.filters?.['amount']),
      type: this.getFilterValue(event.filters?.['type'])?.code,
      createdAt: event.filters?.['createdAt'] && typeof event.filters['createdAt'] === 'object'
        ? new Date((event.filters['createdAt'] as any).value)
        : null,
    };

    this.transactionListService.getMyTransactions(event.first! / 10, event.rows!, event.sortField!, event.sortOrder!, filters)
      .subscribe(
      transactions => {
        this.transactions = transactions.content
        this.totalRecords = transactions.totalElements;
      }
    )
  }

  getFilterValue = (filter: any) => {
    return filter && 'value' in filter ? filter.value : null;
  };

  ngOnDestroy(): void {
    this.destroy$.next(true)
  }
}

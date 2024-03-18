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
    this.transactionListService.getMyTransactions(this.lazyLoadEvent.first!,this.lazyLoadEvent.rows!, this.sort).subscribe(
      transactions => {
        this.transactions = transactions.content
        this.totalRecords = transactions.totalElements;
      }
    )
  }

  ngOnDestroy(): void {
    this.destroy$.next(true)
  }
}

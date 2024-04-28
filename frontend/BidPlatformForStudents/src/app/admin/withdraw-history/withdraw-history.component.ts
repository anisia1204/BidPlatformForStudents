import {Component, inject, OnDestroy, OnInit} from '@angular/core';
import {GoBackService} from "../../utils/go-back.service";
import {TableLazyLoadEvent} from "primeng/table";
import {Subject, takeUntil} from "rxjs";
import {WithdrawHistoryService} from "./withdraw-history.service";

@Component({
  selector: 'app-withdraw-history',
  templateUrl: './withdraw-history.component.html',
  styleUrls: ['./withdraw-history.component.scss']
})
export class WithdrawHistoryComponent implements OnInit, OnDestroy {
  withdrawals!: any[];
  goBackService = inject(GoBackService)
  withdrawHistoryService = inject(WithdrawHistoryService)
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

    const filters = {
      userFullName: this.getFilterValue(event.filters?.['userFullName']),
      points: this.getFilterValue(event.filters?.['points']),
      createdAt: event.filters?.['createdAt'] && typeof event.filters['createdAt'] === 'object'
          ? new Date((event.filters['createdAt'] as any).value)
          : null,
    };

    this.withdrawHistoryService.getAll(event.first! / 10, event.rows!, event.sortField!, event.sortOrder!, filters)
        .subscribe(
            withdrawals => {
              this.withdrawals = withdrawals.content
              this.totalRecords = withdrawals.totalElements;
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

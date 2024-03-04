import {Component, EventEmitter, inject, Input, OnInit, Output} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {Subscription} from "rxjs";
import {DialogService} from "primeng/dynamicdialog";

@Component({
  selector: 'app-announcement-list',
  templateUrl: './announcement-list.component.html',
  styleUrls: ['./announcement-list.component.scss'],
  providers: [DialogService]
})
export class AnnouncementListComponent implements OnInit {
    @Input() announcements : any[] | undefined //Page<AnnouncementVoModel> | null = null;
    @Output() lazyLoad = new EventEmitter<{page: number, size: number}>();
    @Input() totalRecords: number | undefined;
    @Output() delete = new EventEmitter<number>();

  route = inject(ActivatedRoute)
    router = inject(Router)
    routeQueryParams$: Subscription | undefined;
    visible = false;

  onLazyLoad(page: number, size: any) {
    this.lazyLoad.emit({page, size});
  }

  ngOnInit(): void {
    this.routeQueryParams$ = this.route.queryParams.subscribe(params => {
      this.visible = !!params['dialog'];
    });
  }

  closeDialog() {
    this.visible = false
    this.router.navigate(['/', 'my-announcements'])
  }

  onDelete(id: number) {
    this.delete.emit(id)
  }
}

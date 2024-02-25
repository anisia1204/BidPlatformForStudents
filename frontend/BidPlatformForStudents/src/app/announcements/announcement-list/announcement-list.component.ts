import {Component, EventEmitter, inject, Input, OnInit, Output} from '@angular/core';
import {MenuItem} from "primeng/api";
import {ActivatedRoute} from "@angular/router";
import {Subscription} from "rxjs";
import {NewAnnouncementComponent} from "../new-announcement/new-announcement.component";
import {DialogService, DynamicDialogRef} from "primeng/dynamicdialog";

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
    items: MenuItem[] | null = null;
    route = inject(ActivatedRoute)
  routeQueryParams$: Subscription | undefined;
  constructor(public dialogService: DialogService) {}

  onLazyLoad(page: number, size: any) {
    this.lazyLoad.emit({page, size});
  }

  ngOnInit(): void {
    this.items = [
      {
        icon: 'pi pi-pencil',
        tooltipOptions: {
          tooltipLabel: 'Editeaza'
        },
      },
      {
        icon: 'pi pi-trash',
        tooltipOptions: {
          tooltipLabel: 'Sterge'
        },
      }
    ]
    this.routeQueryParams$ = this.route.queryParams.subscribe(params => {
      console.log(params)
      if (params['dialog']) {
        console.log("a")

        this.openDialog();
      }
    });
  }
  newAnnouncementDialog: DynamicDialogRef | undefined;

  private openDialog() {
    this.newAnnouncementDialog = this.dialogService.open(NewAnnouncementComponent, {
      position: "center",
      modal: true,
    });
  }
}

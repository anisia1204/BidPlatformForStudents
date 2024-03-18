import {Component, EventEmitter, inject, Input, OnInit, Output} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {Subscription} from "rxjs";
import {DialogService} from "primeng/dynamicdialog";
import {ConfirmationService} from "primeng/api";

@Component({
  selector: 'app-announcement-list',
  templateUrl: './announcement-list.component.html',
  styleUrls: ['./announcement-list.component.scss'],
  providers: [DialogService, ConfirmationService]
})
export class AnnouncementListComponent implements OnInit {
    @Input() announcements : any[] | undefined //Page<AnnouncementVoModel> | null = null;
    @Input() myAnnouncements = false;
    @Output() lazyLoad = new EventEmitter<{page: number, size: number}>();
    @Input() totalRecords: number | undefined;
    @Output() delete = new EventEmitter<number>();
    @Output() favorites = new EventEmitter<number>();

  route = inject(ActivatedRoute)
    router = inject(Router)
    routeQueryParams$: Subscription | undefined;
    visible = false;
  favorite = false;
  constructor(private confirmationService: ConfirmationService) {}

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
    this.confirmationService.confirm({
      message: 'Esti sigur ca vrei sa stergi anuntul? Aceasta actiune este ireversibila!',
      header: 'Confirmare de stergere',
      icon: 'pi pi-info-circle',
      acceptButtonStyleClass:"p-button-danger p-button-text",
      rejectButtonStyleClass:"p-button-text p-button-text",
      acceptIcon:"none",
      rejectIcon:"none",
      accept: () => {
        this.delete.emit(id)
      }
    });

  }

  onAddToFavorites(id: number) {
    this.favorites.emit(id);
    this.favorite = !this.favorite;
  }
}

import {Component, EventEmitter, inject, Input, OnDestroy, OnInit, Output} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {Subject, Subscription, takeUntil} from "rxjs";
import {DialogService} from "primeng/dynamicdialog";
import {ConfirmationService} from "primeng/api";
import {UserContextService} from "../../auth/user-context-service/user-context.service";
import {FavoriteAnnouncementVoModel} from "../domain/favorite-announcement-vo.model";
import {FavoriteAnnouncementDtoModel} from "../domain/favorite-announcement-dto.model";

@Component({
  selector: 'app-announcement-list',
  templateUrl: './announcement-list.component.html',
  styleUrls: ['./announcement-list.component.scss'],
  providers: [DialogService, ConfirmationService]
})
export class AnnouncementListComponent implements OnInit, OnDestroy {
    @Input() announcements : any[] | undefined
    @Input() myAnnouncements = false;
    @Input() title : string | null = null;
    @Output() lazyLoad = new EventEmitter<{page: number, size: number}>();
    @Input() totalRecords: number | undefined;
    @Output() delete = new EventEmitter<number>();
    @Output() buy = new EventEmitter<number>();
    @Output() addFavorite = new EventEmitter<FavoriteAnnouncementDtoModel>();
    @Output() removeFavorite = new EventEmitter<number>();
    destroy$: Subject<boolean> = new Subject<boolean>()
    myId: number | undefined

    route = inject(ActivatedRoute)
    router = inject(Router)
    userContextService = inject(UserContextService)
    routeQueryParams$: Subscription | undefined;
    visible = false;
    constructor(private confirmationService: ConfirmationService) {}

    onLazyLoad(page: number, size: any) {
      this.lazyLoad.emit({page, size});
    }

    ngOnInit(): void {
      this.routeQueryParams$ = this.route.queryParams.subscribe(params => {
        this.visible = !!params['dialog'];
      });
      this.userContextService.getLoggedInUser()
        .pipe(takeUntil(this.destroy$))
        .subscribe(res => this.myId = res?.id)
    }

    closeDialog() {
      this.visible = false
      if(this.myAnnouncements) {
        this.router.navigate(['my-announcements'])
      }
      else {
        this.router.navigate(['dashboard'])
      }
    // const currentUrl = this.router.url;
    // const parts = currentUrl.split('/');
    // parts.pop(); // Remove the last segment
    // const parentUrl = parts.join('/');
    // this.router.navigateByUrl(parentUrl);
    }

    onDelete(event: MouseEvent, id: number) {
      event.stopPropagation();
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

    onEdit(event: MouseEvent, id: number, announcementType: string) {
      event.stopPropagation();
      this.router.navigate(['edit', id], {
        relativeTo: this.route,
        state: {type: announcementType},
        queryParams: {dialog: true}
      })
    }

    isUsersFavorite(favoriteAnnouncementVOS: FavoriteAnnouncementVoModel[]) {
      return favoriteAnnouncementVOS.some(favoriteAnnouncementVo => favoriteAnnouncementVo.userId === this.myId)
    }

    onRemoveFromFavorites(event: MouseEvent, favoriteAnnouncementVOS: FavoriteAnnouncementVoModel[]) {
      event.stopPropagation();
      const favoriteAnnouncementVO = favoriteAnnouncementVOS.find(favoriteAnnouncementVo => favoriteAnnouncementVo.userId === this.myId)
      this.removeFavorite.emit(favoriteAnnouncementVO?.id)
    }
    onAddToFavorites(event: MouseEvent, id: number) {
      event.stopPropagation();
      const favoriteAnnouncementDto = new FavoriteAnnouncementDtoModel()
      favoriteAnnouncementDto.announcementId = id
      favoriteAnnouncementDto.userId = this.myId
      this.addFavorite.emit(favoriteAnnouncementDto);
    }

    onBuy(event: MouseEvent, id: number, announcementType: any) {
      event.stopPropagation();
      if(announcementType === 'project') {
        this.router.navigate(['project-transaction'], {
          relativeTo: this.route,
          state: {id: id},
          queryParams: {dialog: true}
        })
      }
      else {
        this.confirmationService.confirm({
          message: 'Doresti sa cumperi anuntul? Aceasta actiune este ireversibila!',
          header: 'Confirmare de cumparare',
          icon: 'pi pi-info-circle',
          acceptButtonStyleClass:"p-button-success p-button-text",
          rejectButtonStyleClass:"p-button-danger p-button-text",
          acceptIcon:"none",
          rejectIcon:"none",
          accept: () => {
            this.buy.emit(id)
          }
          });
    }
    }

    ngOnDestroy(): void {
      this.destroy$.next(true)
    }
}

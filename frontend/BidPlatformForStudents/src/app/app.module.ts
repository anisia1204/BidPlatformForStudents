import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppComponent } from './app.component';
import { LoginComponent } from './auth/login/login.component';
import {CardModule} from "primeng/card";
import {ButtonModule} from "primeng/button";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {ChipsModule} from "primeng/chips";
import { RouterLink, RouterModule, Routes} from "@angular/router";
import { RegistrationComponent } from './auth/registration/registration.component';
import {ToastModule} from "primeng/toast";
import {ConfirmDialogModule} from "primeng/confirmdialog";
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {CommonModule} from "@angular/common";
import {HTTP_INTERCEPTORS, HttpClientModule} from "@angular/common/http";
import { NavbarComponent } from './infra/navbar/navbar.component';
import {MenubarModule} from "primeng/menubar";
import { DashboardComponent } from './dashboard/dashboard.component';
import {SplitButtonModule} from "primeng/splitbutton";
import { ProfileComponent } from './profile/profile.component';
import { NewAnnouncementComponent } from './announcements/new-announcement/new-announcement.component';
import { AnnouncementListComponent } from './announcements/announcement-list/announcement-list.component';
import { TransactionListComponent } from './transactions/transaction-list/transaction-list.component';
import { PageNotFoundComponent } from './infra/page-not-found/page-not-found.component';
import {AuthInterceptorService} from "./auth/auth-interceptor/auth-interceptor.service";
import {authGuardFn} from "./auth/auth-guard/auth.guard";
import { TeachingMaterialFormComponent } from './announcements/teaching-material-form/teaching-material-form.component';
import { TutoringServiceFormComponent } from './announcements/tutoring-service-form/tutoring-service-form.component';
import {InputTextareaModule} from "primeng/inputtextarea";
import {RadioButtonModule} from "primeng/radiobutton";
import { ProjectInputsComponent } from './announcements/project-inputs/project-inputs.component';
import {CalendarModule} from "primeng/calendar";
import {DropdownModule} from "primeng/dropdown";
import {StyleClassModule} from "primeng/styleclass";
import {FileUploadModule} from "primeng/fileupload";
import { MyAnnouncementsComponent } from './announcements/my-announcements/my-announcements.component';
import {DataViewModule} from "primeng/dataview";
import {TagModule} from "primeng/tag";
import {CarouselModule} from "primeng/carousel";
import {GalleriaModule} from "primeng/galleria";
import {SpeedDialModule} from "primeng/speeddial";
import {DialogModule} from "primeng/dialog";
import { AnnouncementDetailsComponent } from './announcements/announcement-details/announcement-details.component';
import {TableModule} from "primeng/table";
import { ProjectTransactionComponent } from './transactions/project-transaction/project-transaction.component';
import {AvatarModule} from "primeng/avatar";
import {OverlayPanelModule} from "primeng/overlaypanel";
import { ChatPageComponent } from './chat/chat-page/chat-page.component';
import { ChatRoomComponent } from './chat/chat-page/chat-room/chat-room.component';
import { ChatListComponent } from './chat/chat-page/chat-list/chat-list.component';
import { FavoriteAnnouncementsListComponent } from './announcements/favorite-announcements-list/favorite-announcements-list.component';
import {RippleModule} from "primeng/ripple";
import { SidebarComponent } from './infra/sidebar/sidebar.component';
import {SidebarModule} from "primeng/sidebar";
import {MenuModule} from "primeng/menu";
import {ChartModule} from "primeng/chart";
import { AnnouncementListFilterComponent } from './announcements/announcement-list/announcement-list-filter/announcement-list-filter.component';
import {SliderModule} from "primeng/slider";
import { AnnouncementListSortComponent } from './announcements/announcement-list/announcement-list-sort/announcement-list-sort.component';
import { QrCodeScannerComponent } from './admin/qr-code-scanner/qr-code-scanner.component';
import {NgxScannerQrcodeModule} from "ngx-scanner-qrcode";
import {ZXingScannerModule} from "@zxing/ngx-scanner";
import { UpdateUserPointsComponent } from './admin/update-user-points/update-user-points.component';
import {Role} from "./auth/domain/role";
import { UnauthorizedPageComponent } from './infra/unauthorized-page/unauthorized-page.component';

const routes: Routes = [
  {
    path: "",
    redirectTo: "login",
    pathMatch: "full",
  },
  {
    path: "login",
    component: LoginComponent
  },
  {
    path: "dashboard",
    component: DashboardComponent,
    canActivate: [authGuardFn],
    data: {
      role: Role.USER
    },
    children: [
      {
        path: "details",
        component: AnnouncementDetailsComponent
      },
      {
        path: 'project-transaction',
        component: ProjectTransactionComponent
      }
    ]
  },
  {
    path:"scan-qr",
    canActivate: [authGuardFn],
    data: {
      role: Role.ADMIN
    },
    component: QrCodeScannerComponent
  },
  {
    path:"profile",
    canActivate: [authGuardFn],
    data: {
      role: Role.USER
    },
    component: ProfileComponent
  },
  {
    path: "new-announcement",
    canActivate: [authGuardFn],
    data: {
      role: Role.USER
    },
    component: NewAnnouncementComponent
  },
  {
    path: "chat",
    canActivate: [authGuardFn],
    data: {
      role: Role.USER
    },
    component: ChatPageComponent
  },
  {
    path: "my-announcements",
    canActivate: [authGuardFn],
    data: {
      role: Role.USER
    },
    component: MyAnnouncementsComponent,
    children: [
      {
        path: "edit/:id",
        component: NewAnnouncementComponent
      },
      {
        path: "details",
        component: AnnouncementDetailsComponent
      }
    ]
  },
  {
    path: "favorites",
    canActivate: [authGuardFn],
    data: {
      role: Role.USER
    },
    component: FavoriteAnnouncementsListComponent,
    children: [
      {
        path: 'project-transaction',
        component: ProjectTransactionComponent
      },
      {
        path: "details",
        component: AnnouncementDetailsComponent
      }
    ]
  },
  {
    path: "my-transactions",
    canActivate: [authGuardFn],
    data: {
      role: Role.USER
    },
    component: TransactionListComponent
  },
  {
    path:"update/:userId",
    canActivate: [authGuardFn],
    data: {
      role: Role.ADMIN
    },
    component: UpdateUserPointsComponent
  },
  {
    path: "**",
    component: PageNotFoundComponent
  }
]

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    RegistrationComponent,
    NavbarComponent,
    DashboardComponent,
    ProfileComponent,
    NewAnnouncementComponent,
    AnnouncementListComponent,
    TransactionListComponent,
    PageNotFoundComponent,
    TeachingMaterialFormComponent,
    TutoringServiceFormComponent,
    ProjectInputsComponent,
    MyAnnouncementsComponent,
    AnnouncementDetailsComponent,
    ProjectTransactionComponent,
    ChatPageComponent,
    ChatRoomComponent,
    ChatListComponent,
    FavoriteAnnouncementsListComponent,
    SidebarComponent,
    AnnouncementListFilterComponent,
    AnnouncementListSortComponent,
    QrCodeScannerComponent,
    UpdateUserPointsComponent,
    UnauthorizedPageComponent,
  ],
  imports: [
    BrowserModule,
    CardModule,
    ButtonModule,
    ReactiveFormsModule,
    ChipsModule,
    RouterLink,
    RouterModule.forRoot(routes),
    ToastModule,
    ConfirmDialogModule,
    BrowserAnimationsModule,
    CommonModule,
    HttpClientModule,
    MenubarModule,
    SplitButtonModule,
    InputTextareaModule,
    RadioButtonModule,
    FormsModule,
    CalendarModule,
    DropdownModule,
    StyleClassModule,
    FileUploadModule,
    DataViewModule,
    TagModule,
    CarouselModule,
    GalleriaModule,
    SpeedDialModule,
    DialogModule,
    TableModule,
    AvatarModule,
    OverlayPanelModule,
    RippleModule,
    SidebarModule,
    MenuModule,
    ChartModule,
    SliderModule,
    NgxScannerQrcodeModule,
    ZXingScannerModule
  ],
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: AuthInterceptorService,
      multi: true
    }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }

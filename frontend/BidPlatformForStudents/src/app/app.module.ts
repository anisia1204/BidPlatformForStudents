import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppComponent } from './app.component';
import { LoginComponent } from './auth/login/login.component';
import {CardModule} from "primeng/card";
import {ButtonModule} from "primeng/button";
import {ReactiveFormsModule} from "@angular/forms";
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
import { EditAnnouncementComponent } from './announcements/edit-announcement/edit-announcement.component';
import { AnnouncementListComponent } from './announcements/announcement-list/announcement-list.component';
import { TransactionListComponent } from './transactions/transaction-list/transaction-list.component';
import { FavouritesListComponent } from './favourites/favourites-list/favourites-list.component';
import { PageNotFoundComponent } from './infra/page-not-found/page-not-found.component';
import {AuthInterceptorService} from "./auth/auth-interceptor/auth-interceptor.service";
import {authGuardFn} from "./auth/auth-guard/auth.guard";

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
    canActivate: [authGuardFn]
  },
  {
    path:"profile",
    component: ProfileComponent
  },
  {
    path: "new-announcement",
    component: NewAnnouncementComponent
  },
  {
    path: "my-announcements",
    component: AnnouncementListComponent
  },
  {
    path: "favourites",
    component: FavouritesListComponent
  },
  {
    path: "my-transactions",
    component: TransactionListComponent
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
    EditAnnouncementComponent,
    AnnouncementListComponent,
    TransactionListComponent,
    FavouritesListComponent,
    PageNotFoundComponent
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
        SplitButtonModule
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

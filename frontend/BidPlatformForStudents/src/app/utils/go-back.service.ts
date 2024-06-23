import {inject, Injectable} from '@angular/core';
import {BehaviorSubject, filter, map, Observable} from 'rxjs';
import {NavigationEnd, Router} from "@angular/router";
import {Location} from "@angular/common";

@Injectable({
  providedIn: 'root'
})
export class GoBackService {
  private history: string[] = [];
  private latestRouterState:BehaviorSubject<any> = new BehaviorSubject<any>(null);
  router = inject(Router)
  location = inject(Location)

  constructor() {
    this.initializeRouterEvents();
  }

  goBack(data?: any, preserveRouterOutletHistoryOrder = false): void {
    const isRouterOutlet= (this.history.length >= 1) ? this.history[this.history.length-1].search(/\s*\(.?\)\s/g) != -1 : false;
    if (!isRouterOutlet && this.history.length <= 1) {
      this.location.back();
    } else {
      const lastUrl = this.history[this.history.length-(isRouterOutlet && !preserveRouterOutletHistoryOrder ? 1 : 2)];
      let navUrl = lastUrl;
      if (!preserveRouterOutletHistoryOrder) {
        const filtered = lastUrl.replace(/\s*\(.?\)\s/g, '');
        navUrl = lastUrl.search(/\s*\(.?\)\s/g) ? filtered : '/';
      }
      if (data) {
        this.router.navigateByUrl(navUrl, {state: data});
      } else {
        this.router.navigateByUrl(navUrl);
      }
    }
  }

  getNavData(): Observable<{ [k: string]: any }> {
    return this.router.events.pipe(
      filter((event) => event instanceof NavigationEnd),
      map(() => this.router.getCurrentNavigation()?.extras.state || {}),
      filter((res) => res != null),
    );
  }

  private initializeRouterEvents() {
    this.router.events.subscribe((event) => {
      if (event instanceof NavigationEnd) {
        this.history.push(event.urlAfterRedirects)
        this.latestRouterState.next(
          this.router.getCurrentNavigation()?.extras.state
        );
      }
    });
  }
}

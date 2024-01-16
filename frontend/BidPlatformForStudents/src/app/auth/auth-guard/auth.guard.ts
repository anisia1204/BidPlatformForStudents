import {
  ActivatedRouteSnapshot,
  CanActivateFn,
  Router,
  RouterStateSnapshot,
  UrlTree
} from "@angular/router";
import {map, Observable, take} from "rxjs";
import {LoginService} from "../login/login.service";
import {inject} from "@angular/core";

export const authGuardFn : CanActivateFn =
  (route: ActivatedRouteSnapshot, state: RouterStateSnapshot) : Observable<boolean|UrlTree> => {
    const router = inject(Router);
    const loginService = inject(LoginService);
    return loginService.loggedInUser$.pipe(
      take(1),
      map(
        user => {
          const isAuth = !!user;
          if(isAuth) {
            return true;
          }
          return router.createUrlTree(['/login'])
        }
      )
    );
  };

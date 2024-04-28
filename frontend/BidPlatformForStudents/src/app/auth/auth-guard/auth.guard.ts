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
import {Role} from "../domain/role";

export const authGuardFn : CanActivateFn =
  (route: ActivatedRouteSnapshot, state: RouterStateSnapshot) : Observable<boolean|UrlTree> => {
    const router = inject(Router);
    const loginService = inject(LoginService);
    return loginService.loggedInUser$.pipe(
      take(1),
      map(
        user => {
            const isAuth = !!user;
            if (isAuth) {
                const requiredRole = route.data["role"] as Role;
                if (requiredRole) {
                    const userRole = user?.role;
                    if (userRole !== requiredRole) {
                        return router.createUrlTree(['/unauthorized']);
                    }
                }
                return true;
            }
            return router.createUrlTree(['/login']);
        }
      )
    );
  };

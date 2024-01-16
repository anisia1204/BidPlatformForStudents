import {inject, Injectable} from "@angular/core";
import {LoginService} from "../../auth/login/login.service";

@Injectable({
  providedIn: "root"
})
export class NavbarService {
  loginService = inject(LoginService)

  isLoggedIn() {
    return this.loginService.loggedInUser$;
  }
  logout() {
    this.loginService.isLoggedOut();
  }
}

import {inject, Injectable} from "@angular/core";
import {UserResourceService} from "../resource-service/user-resource.service";
import {UserDtoModel} from "../domain/user-dto.model";
import {BehaviorSubject} from "rxjs";
import {Router} from "@angular/router";
import {LoggedInUserDtoModel} from "../domain/logged-in-user-dto.model";
import {Role} from "../domain/role";

@Injectable({
  providedIn: "root"
})
export class LoginService {
  private loggedInUserSubject = new BehaviorSubject<LoggedInUserDtoModel | null>(null)
  private tokenExpirationTimer: any;
  loggedInUser$ = this.loggedInUserSubject.asObservable()
  userResourceService = inject(UserResourceService)
  router = inject(Router)

  login(userDto: UserDtoModel) {
    return this.userResourceService.login(userDto);
  }

  isLoggedIn(loggedInUserDto: LoggedInUserDtoModel) {

    const user = new LoggedInUserDtoModel(
      loggedInUserDto.id,
      loggedInUserDto.firstName,
      loggedInUserDto.lastName,
      loggedInUserDto.email,
      loggedInUserDto.points,
      loggedInUserDto.role,
      loggedInUserDto.token,
      loggedInUserDto.tokenExpirationDate
    )
    this.loggedInUserSubject.next(user);

    if(loggedInUserDto.tokenExpirationDate) {
      const expiresIn = new Date(loggedInUserDto.tokenExpirationDate).getTime() - new Date().getTime();
      this.autoLogout(expiresIn);
    }

    localStorage.setItem('userData', JSON.stringify(user));
  }
  isLoggedOut() {
    this.loggedInUserSubject.next(null);
    this.router.navigate(['/login'])
    localStorage.removeItem('userData');
    if(this.tokenExpirationTimer) {
      clearTimeout(this.tokenExpirationTimer);
    }
    this.tokenExpirationTimer = null;
  }

  autoLogin() {
    const storedData = localStorage.getItem('userData')
    if(storedData) {
      const userData: {
        id: string;
        firstName: string;
        lastName: string;
        email: string;
        points: string;
        role: Role;
        _token: string;
        _tokenExpirationDate: string;
      } = JSON.parse(storedData);
      if(!userData) {
        return;
      }

      const loadedUser = new LoggedInUserDtoModel(
        Number(userData.id),
        userData.firstName,
        userData.lastName,
        userData.email,
        Number(userData.points),
        userData.role,
        userData._token,
        new Date(userData._tokenExpirationDate)
      );

      if(loadedUser.token) {
        this.loggedInUserSubject.next(loadedUser);
        const expirationDuration = this.getExpirationDurationOfJwtToken(loadedUser.tokenExpirationDate)
        if(expirationDuration) {
          this.autoLogout(expirationDuration)
        }
      }
    }
  }

  autoLogout(expirationDuration: number) {
    this.tokenExpirationTimer = setTimeout(() => {
      this.isLoggedOut()
    }, expirationDuration)
  }

  getExpirationDurationOfJwtToken(tokenExpirationDate: Date | null) {
    if(tokenExpirationDate) {
      return new Date(tokenExpirationDate).getTime() - new Date().getTime();
    }
    else
      return null;
  }
}

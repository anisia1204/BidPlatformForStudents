import {Injectable} from "@angular/core";
import {BehaviorSubject, Observable} from "rxjs";
import {LoggedInUserDtoModel} from "../domain/logged-in-user-dto.model";

@Injectable({
    providedIn: "root"
})
export class UserContextService {
    private loggedInUserSubject = new BehaviorSubject<LoggedInUserDtoModel | null>(null);
    loggedInUser$ = this.loggedInUserSubject.asObservable();

    constructor() {}

    setLoggedInUser(user: LoggedInUserDtoModel) {
        this.loggedInUserSubject.next(user);
    }

    getLoggedInUser(): Observable<LoggedInUserDtoModel | null> {
        return this.loggedInUser$;
    }
}

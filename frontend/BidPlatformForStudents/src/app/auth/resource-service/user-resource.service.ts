import {Injectable} from "@angular/core";
import {UserDtoModel} from "../domain/user-dto.model";
import {HttpClient} from "@angular/common/http";

@Injectable({
  providedIn: "root"
})
export class UserResourceService {
  private url: string = "http://localhost:8081/api/user"
  constructor(private httpClient: HttpClient) {
    this.httpClient = httpClient;
  }

  saveUser(userDto: UserDtoModel) {
    return this.httpClient.post<UserDtoModel>(`${this.url}`, userDto);
  }
}

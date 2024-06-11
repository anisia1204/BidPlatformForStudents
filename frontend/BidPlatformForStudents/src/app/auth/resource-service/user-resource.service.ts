import {Injectable} from "@angular/core";
import {UserDtoModel} from "../domain/user-dto.model";
import {HttpClient, HttpParams} from "@angular/common/http";
import {LoggedInUserDtoModel} from "../domain/logged-in-user-dto.model";
import {UserVoModel} from "../domain/user-vo.model";
import {UserEmailVoModel} from "../domain/user-email-vo.model";
import {ProfilePictureDtoModel} from "../domain/profile-picture-dto.model";

@Injectable({
    providedIn: "root"
})
export class UserResourceService {
    private url: string = "http://localhost:8081/api/user"

    constructor(private httpClient: HttpClient) {
        this.httpClient = httpClient;
    }

    saveUser(userDto: UserDtoModel, files: File[]) {
        const formData: FormData = new FormData();
        for (let file of files) {
            formData.append('file', file);
        }
        formData.append('userDTOString', new Blob([JSON.stringify(userDto)], {
            type: "application/json"
        }));
        return this.httpClient.post<UserDtoModel>(`${this.url}`, formData);
    }

    updateUserProfilePic(files: File[]) {
      const formData: FormData = new FormData();
      for (let file of files) {
        formData.append('file', file);
      }
      return this.httpClient.put<ProfilePictureDtoModel>(`${this.url}/profile-pic`, formData);
    }

    login(userDto: UserDtoModel) {
        return this.httpClient.post<LoggedInUserDtoModel>(`${this.url}/login`, userDto)
    }

    getProfileDetails() {
        return this.httpClient.get<UserVoModel>(`${this.url}/profile`);
    }

    editProfileDetails(userDto: UserDtoModel) {
        return this.httpClient.put<UserDtoModel>(`${this.url}`, userDto);
    }

    getFilteredUserEmails(email: string) {
      let params = new HttpParams().set('email', email);
      return this.httpClient.get<UserEmailVoModel>(`${this.url}/email-list`, {params});
    }
}

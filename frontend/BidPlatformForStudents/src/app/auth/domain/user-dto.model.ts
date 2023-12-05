import {AuthenticationResponseModel} from "./authentication-response.model";

export class UserDtoModel {
  firstName: string | undefined;
  lastName: string | undefined;
  email: string | undefined;
  password: string | undefined;
  _authenticationResponse: AuthenticationResponseModel | undefined;

  get authenticationResponse() {
    return this._authenticationResponse
  }
}

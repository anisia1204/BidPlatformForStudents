import {AuthenticationResponseModel} from "./authentication-response.model";

export class UserDtoModel {
  firstName?: string;
  lastName?: string;
  email?: string;
  password?: string;
  authenticationResponse?: AuthenticationResponseModel;
}

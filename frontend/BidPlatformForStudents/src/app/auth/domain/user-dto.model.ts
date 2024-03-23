
export class UserDtoModel {
  constructor(
  public firstName: string | undefined,
  public lastName: string | undefined,
  public email: string | undefined,
  public password: string | undefined) {}


  // get authenticationResponse() {
  //   if(!this._authenticationResponse?.tokenExpirationDate || new Date() > this._authenticationResponse.tokenExpirationDate)
  //     return null;
  //   return this._authenticationResponse;
  // }

}

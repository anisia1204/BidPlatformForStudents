export class LoggedInUserDtoModel {
  constructor(
    public id: number | undefined,
    public firstName: string | undefined,
    public lastName: string | undefined,
    public email: string | undefined,
    public points: number | undefined,
    private _token: string | null,
    private _tokenExpirationDate: Date | null) {}

  get token() {
    if(!this.tokenExpirationDate || new Date() > this.tokenExpirationDate)
      return null;
    return this._token
  }

  get tokenExpirationDate() {
    return this._tokenExpirationDate
  }

}

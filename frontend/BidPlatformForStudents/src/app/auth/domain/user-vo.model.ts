import {ProfilePictureVoModel} from "./profile-picture-vo.model";
import {QrCodeVoModel} from "./qr-code-vo.model";

export class UserVoModel {
    public id: number | undefined
    public firstName: string | undefined
    public lastName: string | undefined
    public email: string | undefined
    public points: number | undefined
    public createdAt: string | undefined
    public profilePictureVO: ProfilePictureVoModel | undefined
    public qrCodeVO: QrCodeVoModel | undefined

}

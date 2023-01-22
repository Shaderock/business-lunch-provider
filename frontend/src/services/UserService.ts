import axios from "axios";
import {AppSettings} from "./AppSettings";

export class UserService {
  private profileUrl = AppSettings.API_URL + '/users/profile';

  public getProfile(): Promise<any> {
    return axios.get(this.profileUrl)
  }
}

const userService: UserService = new UserService()
export default userService

import axios from "axios";
import {AppSettings} from "./AppSettings";

export class UserService {
  private profileUrl = AppSettings.API_URL + '/user/profile';
  private sysAdmUsersUrl = AppSettings.API_URL + '/sysadm/user';
  private sysAdmUsersDetailsUrl = AppSettings.API_URL + '/sysadm/user/details';
  private profileDetailsUrl = AppSettings.API_URL + '/user/profile/details';
  private profilePreferencesUrl = AppSettings.API_URL + '/user/profile/preferences';

  public getProfile(): Promise<any> {
    return axios.get(this.profileUrl)
  }

  public getProfileDetails(): Promise<any> {
    return axios.get(this.profileDetailsUrl)
  }

  public getProfilePreferences(): Promise<any> {
    return axios.get(this.profilePreferencesUrl)
  }

  async getAllUsers(): Promise<any> {
    return axios.get(this.sysAdmUsersUrl)
  }

  async getAllUsersDetails(): Promise<any> {
    return axios.get(this.sysAdmUsersDetailsUrl)
  }
}

const userService: UserService = new UserService()
export default userService

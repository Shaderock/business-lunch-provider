import axios from "axios";
import {ApiConstants} from "@/services/ApiConstants";

export class UserService {
  public getProfile(): Promise<any> {
    return axios.get(ApiConstants.USER + '/profile')
  }

  public getProfileDetails(): Promise<any> {
    return axios.get(ApiConstants.USER_DETAILS + '/profile')
  }

  public getProfilePreferences(): Promise<any> {
    return axios.get(ApiConstants.USER_EMPLOYEE_PREFERENCES)
  }

  async getAllUsers(): Promise<any> {
    return axios.get(ApiConstants.SYS_ADM_USER)
  }

  async getAllUsersDetails(): Promise<any> {
    return axios.get(ApiConstants.SYS_ADM_USER_DETAILS)
  }
}

const userService: UserService = new UserService()
export default userService

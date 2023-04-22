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

  async requestEmployeesDetails(): Promise<any> {
    return axios.get(ApiConstants.COMPANY_ADM_USER_DETAILS + "/all")
  }

  async getAllUsersDetails(): Promise<any> {
    return axios.get(ApiConstants.SYS_ADM_USER_DETAILS)
  }

  async deleteEmployee(userEmail: string) {
    const params: URLSearchParams = new URLSearchParams([['userEmail', userEmail]])
    return axios.post(ApiConstants.COMPANY_ADM_COMPANY + "/remove-user", null, {params})
  }

  async grantAdmin(userEmail: string) {
    const params: URLSearchParams = new URLSearchParams([['userEmail', userEmail]])
    return axios.post(ApiConstants.COMPANY_ADM_COMPANY + "/grant-company-admin-rights", null, {params})
  }

  async revokeAdmin(userEmail: string) {
    const params: URLSearchParams = new URLSearchParams([['userEmail', userEmail]])
    return axios.post(ApiConstants.COMPANY_ADM_COMPANY + "/revoke-company-admin-rights", null, {params})
  }
}

const userService: UserService = new UserService()
export default userService

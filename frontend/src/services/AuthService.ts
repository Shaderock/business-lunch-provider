import axios, {AxiosResponse} from "axios";
import {ApiConstants} from "@/services/ApiConstants";

export class AuthService {
  login(email: string, password: string): Promise<AxiosResponse<any>> {
    return axios.post(ApiConstants.LOGIN, {
      email: email,
      password: password
    })
  }

  register(email: string, password: string, firstName: string, lastName: string) {
    return axios.post(ApiConstants.REGISTER, {
      email: email,
      password: password,
      firstName: firstName,
      lastName: lastName
    })
  }

  verifyUserNotRegistered(email: string) {
    const params: URLSearchParams = new URLSearchParams([['email', email]])
    return axios.get(`${ApiConstants.REGISTER}/verify-user`, {params});
  }
}

const authService: AuthService = new AuthService()
export default authService

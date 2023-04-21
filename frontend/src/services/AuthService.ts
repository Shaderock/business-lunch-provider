import axios, {AxiosResponse} from "axios";
import {ApiConstants} from "@/services/ApiConstants";

export class AuthService {
  private registerUrl: string = ApiConstants.BACKEND_URL + '/register'
  private verifyUserRegisteredUrl: string = this.registerUrl + '/verify-user'

  login(email: string, password: string): Promise<AxiosResponse<any>> {
    return axios.post(ApiConstants.BACKEND_URL + '/login', {
      email: email,
      password: password
    })
  }

  register(email: string, password: string, firstName: string, lastName: string) {
    return axios.post(this.registerUrl, {
      email: email,
      password: password,
      firstName: firstName,
      lastName: lastName
    })
  }

  verifyUserNotRegistered(email: string) {
    const params: URLSearchParams = new URLSearchParams([['email', email]])
    return axios.get(this.verifyUserRegisteredUrl, {params});
  }
}

const authService: AuthService = new AuthService()
export default authService

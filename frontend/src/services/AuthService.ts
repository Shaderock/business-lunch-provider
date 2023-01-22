import axios from "axios";
import {AppSettings} from "./AppSettings";

export class AuthService {
  private loginUrl = AppSettings.API_URL + '/login'
  private registerUrl = AppSettings.API_URL + '/register'
  private verifyUserRegisteredUrl = this.registerUrl + '/verify-user'

  login(email: string, password: string) {
    return axios.post(this.loginUrl, {
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
    const params = new URLSearchParams([['email', email]])
    return axios.get(this.verifyUserRegisteredUrl, {params});
  }
}

const authService: AuthService = new AuthService()
export default authService

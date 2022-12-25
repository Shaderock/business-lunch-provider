import axios from "axios";
import {AppSettings} from "./AppSettings";
import {Credentials} from "../models/Credentials";
import {DataService} from "./DataService";

export class AuthService {
    private loginUrl = AppSettings.API_URL + '/login'
    private registerUrl = AppSettings.API_URL + '/register'
    private verifyUserRegisteredUrl = this.registerUrl + '/verify-user'
    private dataService: DataService

    constructor() {
        this.dataService = new DataService()
    }

    login(username: string, password: string) {
        const credentials = new Credentials(username, password)
        return axios.post(this.loginUrl, {credentials})
    }

    register(email: string, password: string, firstName: string, lastName: string) {
        return axios.post(this.registerUrl, {
            email: email,
            password: password,
            firstName: firstName,
            lastName: lastName
        })
    }

    hasRole(role: string) {
        let user = this.dataService.getLocalStorageUser();
        if (user && user.roles)
            return user.roles.includes(role)
    }

    isAuthenticated() {
        return this.dataService.getLocalStorageUser() !== null
    }

    verifyUserRegistered(email: string) {
        const params = new URLSearchParams([['email', email]])
        return axios.get(this.verifyUserRegisteredUrl, {params});
    }
}

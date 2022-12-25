import {AppSettings} from "./AppSettings";
import {User} from "../models/User";

export class DataService {
    getAuthHeader() {
        let user: User = this.getLocalStorageUser()

        if (user && user.token) {
            return {Authorization: 'Bearer ' + user.token}
        }

        return {}
    }

    getLocalStorageUser() {
        let userJson = localStorage.getItem(AppSettings.LOCAL_STORAGE_USER)
        if (userJson) {
            return JSON.parse(userJson);
        }
        return null
    }
}

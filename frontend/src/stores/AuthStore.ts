import {defineStore} from "pinia";
import {AuthService} from "../services/AuthService";
import {AppSettings} from "../services/AppSettings";
import {ToastManager} from "../services/ToastManager";

const authService: AuthService = new AuthService()
export const useAuthStore = defineStore('auth', {
    getters: {
        isAuthenticated() {
            return authService.isAuthenticated()
        },
        isAppUser() {
            return authService.hasRole(AppSettings.APP_USER_ROLE)
        },
        isEmployee() {
            return authService.hasRole(AppSettings.EMPLOYEE_ROLE)
        },
        isCompanyAdmin() {
            return authService.hasRole(AppSettings.COMPANY_ADMIN_ROLE)
        },
        isSupplier() {
            return authService.hasRole(AppSettings.SUPPLIER_ROLE)
        }
    },
    actions: {
        login(email: string, password: string) {
            authService
                .login(email, password)
                .then(response => {
                    if (response.data.accessToken) {
                        localStorage.setItem(AppSettings.LOCAL_STORAGE_USER, JSON.stringify(response.data))
                    }
                    return Promise.resolve;
                })
                .catch(reason => {
                    return Promise.reject(reason.data.errMessage)
                })

        },
        logout() {
            localStorage.removeItem(AppSettings.LOCAL_STORAGE_USER)
        },
        async register(email: string, password: string, firstName: string, lastName: string) {
            return authService.register(email, password, firstName, lastName)
        },
        async verifyUserRegistered(email: string): Promise<boolean> {
            return authService
                .verifyUserRegistered(email)
                .then(response => {
                    return Promise.resolve(response.data);
                })
                .catch(error => {
                    let toastManager: ToastManager = new ToastManager();
                    toastManager.showErrorFromErrorResponse(error)
                    return Promise.reject();
                });
        }
    }
})

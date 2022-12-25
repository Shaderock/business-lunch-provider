import {defineStore} from "pinia";
import {AuthService} from "../services/AuthService";
import {AppSettings} from "../services/AppSettings";
import {ToastManager} from "../services/ToastManager";
import {User} from "../models/User";
import axios from "axios";

const authService: AuthService = new AuthService()
const toastManager: ToastManager = new ToastManager()
export const useAuthStore = defineStore('auth', {
    state: () => ({
        // @ts-ignore
        user: JSON.parse(localStorage.getItem(AppSettings.LOCAL_STORAGE_USER))
    }),
    getters: {
        isAuthenticated(): boolean {
            return this.user !== null && this.user.token !== null
        },
        isAppUser(): boolean {
            return this.isAuthenticated && this.user.roles.includes(AppSettings.APP_USER_ROLE)
        },
        isEmployee(): boolean {
            return this.isAuthenticated && this.user.roles.includes(AppSettings.EMPLOYEE_ROLE)
        },
        isCompanyAdmin(): boolean {
            return this.isAuthenticated && this.user.roles.includes(AppSettings.COMPANY_ADMIN_ROLE)
        },
        isSupplier(): boolean {
            return this.isAuthenticated && this.user.roles.includes(AppSettings.SUPPLIER_ROLE)
        }
    },
    actions: {
        getLocalStorageUser(): User | null {
            let userJson = localStorage.getItem(AppSettings.LOCAL_STORAGE_USER)
            if (userJson) {
                return JSON.parse(userJson);
            }
            return null
        },
        initializeUser() {
            this.user = this.getLocalStorageUser()
            axios.defaults.headers.common['Authorization'] = this.getAuthHeaderBearer()
        },
        getAuthHeaderBearer(): string | null {
            if (this.isAuthenticated) {
                return 'Bearer ' + this.user.token
            }
            return null
        },
        logout() {
            localStorage.removeItem(AppSettings.LOCAL_STORAGE_USER)
            this.initializeUser()
        },
        async login(email: string, password: string): Promise<any> {
            try {
                let response = await authService.login(email, password)
                if (response.data.token) {
                    localStorage.setItem(AppSettings.LOCAL_STORAGE_USER, JSON.stringify(response.data))
                }
                this.initializeUser()
                return Promise.resolve
            } catch (reason) {
                toastManager.showError('Sign in problem',
                    "Couldn't sign you in. Could be wrong credentials.")
                return Promise.reject(reason)
            }
        },
        async register(email: string, password: string, firstName: string, lastName: string): Promise<any> {
            return authService.register(email, password, firstName, lastName)
        },
        async verifyUserRegistered(email: string): Promise<boolean> {
            return authService
                .verifyUserRegistered(email)
                .then(response => {
                    return Promise.resolve(response.data);
                })
                .catch(error => {
                    toastManager.showErrorFromErrorResponse(error)
                    return Promise.reject();
                });
        }
    }
})

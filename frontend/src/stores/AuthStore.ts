import {defineStore} from "pinia";
import {AuthService} from "../services/AuthService";
import {useUserStore} from "./UserStore";

const authService: AuthService = new AuthService()
export const useAuthStore = defineStore('auth', {
    state: () => ({
        jwtToken: null as string | null
    }),
    getters: {
        isAuthenticated(): boolean {
            const userStore = useUserStore();
            return this.jwtToken !== null && userStore.hasUser
        }
    },
    actions: {
        initializeToken() {
            this.jwtToken = this.getLocalStorageToken()
        },
        getLocalStorageToken(): string | null {
            return this.jwtToken = localStorage.getItem("token")
        },
        getToken(): string | null {
            return this.jwtToken;
        },
        getAuthHeaderBearer(): string | null {
            const token = this.getToken();
            if (token) {
                return 'Bearer ' + token.toString()
            }
            return null
        },
        logout() {
            localStorage.removeItem("token")
            this.initializeToken()
            const userStore = useUserStore();
            userStore.clearUser()
        },
        async login(email: string, password: string): Promise<any> {
            let response = await authService.login(email, password)
            if (response.data) {
                localStorage.setItem("token", response.data.token)
            } else {
                return Promise.reject
            }
            this.initializeToken()
            const userStore = useUserStore();
            await userStore.requestUserData()
            return Promise.resolve
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
                .catch(() => {
                    return Promise.reject();
                });
        }
    }
})

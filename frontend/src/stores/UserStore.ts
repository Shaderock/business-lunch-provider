import {defineStore} from "pinia";
import {User} from "../models/User";
import {RoleService} from "../services/RoleService";
import {Role} from "../models/Role";
import {ToastManager} from "../services/ToastManager";
import {UserService} from "../services/UserService";

const roleService = new RoleService()
const toastManager: ToastManager = new ToastManager();
const userService: UserService = new UserService();
export const useUserStore = defineStore('user', {
    state: () => ({
        user: null as User | null
    }),
    getters: {
        getUser(): User | null {
            return this.user
        },
        hasUser(): boolean {
            return this.user !== null;
        },
        isAppUser(): boolean {
            return roleService.hasRole(this.user, Role.User)
        },
        isOnlyAppUser(): boolean {
            return roleService.hasOnlyRole(this.user, Role.User)
        },
        isEmployee(): boolean {
            return roleService.hasRole(this.user, Role.Employee)
        },
        isCompanyAdmin(): boolean {
            return roleService.hasRole(this.user, Role.CompanyAdmin)
        },
        isSupplier(): boolean {
            return roleService.hasRole(this.user, Role.Supplier)
        }
    },
    actions: {
        clearUser() {
            this.user = null;
        },
        async requestUserData() {
            try {
                let response = await userService.getProfile();
                this.user = response.data;
            } catch (error) {
                toastManager.showError("Profile not loaded", "Couldn't load profile data. Try again later.")
            }
        },
    }
})

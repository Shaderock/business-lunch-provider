// Utilities
import {defineStore} from 'pinia'
import {AuthService} from "@/services/AuthService";
import {CompanyService} from "@/services/CompanyService";
import {RoleService} from "@/services/RoleService";
import {Company} from "@/models/Company";
import {CompanyPreferences} from "@/models/CompanyPreferences";
import {User} from "@/models/User";
import {Role} from "@/models/Role";
import {UserService} from "@/services/UserService";
import {ToastManager} from "@/services/ToastManager";

const authService: AuthService = new AuthService()
const companyService: CompanyService = new CompanyService()
const roleService: RoleService = new RoleService()
const userService: UserService = new UserService()
const toastManager: ToastManager = new ToastManager()

export const useAuthStore = defineStore('auth', {
  state: () => ({
    jwtToken: null as string | null
  }),
  getters: {
    hasToken(): boolean {
      return this.jwtToken !== null
    },
    isAuthenticated(): boolean {
      const userStore = useUserStore();
      return this.hasToken && userStore.hasUser
    }
  },
  actions: {
    initializeToken() {
      this.jwtToken = this.getLocalStorageToken()
    },
    getLocalStorageToken(): string | null {
      let token = localStorage.getItem("token");
      return this.jwtToken = token
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
    }
  }
})

export const useCompanyStore = defineStore('company', {
  state: () => ({
    company: null as Company | null,
    companyPreferences: null as CompanyPreferences | null
  }),
  getters: {
    getCompany(): Company | null {
      return this.company
    },
    getCompanyPreferences(): CompanyPreferences | null {
      return this.companyPreferences
    },
    hasCompany(): boolean {
      return this.company !== null;
    },
    hasCompanyPreferences(): boolean {
      return this.companyPreferences !== null;
    },
  },
  actions: {
    clearCompany() {
      this.company = null
    },
    clearCompanyPreferences() {
      return this.companyPreferences
    },
    async requestData() {
      let response = await companyService.getUserCompany()
      this.company = response.data
      //     todo load preferences
    }
  }
})

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

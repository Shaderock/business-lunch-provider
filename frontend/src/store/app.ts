// Utilities
import {defineStore} from 'pinia'
import {AuthService} from "@/services/AuthService";
import {CompanyService} from "@/services/CompanyService";
import {RoleService} from "@/services/RoleService";
import {Company} from "@/models/Company";
import {CompanyPreferences} from "@/models/CompanyPreferences";
import {UserDetails} from "@/models/UserDetails";
import {Role} from "@/models/Role";
import {UserService} from "@/services/UserService";
import {ToastManager} from "@/services/ToastManager";
import {User} from "@/models/User";
import {isEqual} from "lodash";
import {Supplier} from "@/models/Supplier";
import {SupplierPreferences} from "@/models/SupplierPreferences";
import supplierService from "@/services/SupplierService";

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
      this.jwtToken = localStorage.getItem("token")
      return this.jwtToken
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
      const response = await authService.login(email, password)
      if (response.data) {
        localStorage.setItem("token", response.data.token)
      } else {
        return Promise.reject
      }
      this.initializeToken()
      await useUserStore().requestUserData()
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
    async requestFreshCompanyData() {
      const response = await companyService.getUserCompany()
      this.company = response.data
      //     todo load preferences
    }
  }
})

export const useSysAdmCompanyStore = defineStore('sysAdmCompany', {
  state: () => ({
    companies: [] as Company[],
    companiesPreferences: [] as CompanyPreferences[]
  }),
  getters: {
    getCompanies(): Company[] {
      return this.companies
    },
    getCompaniesPreferences(): CompanyPreferences {
      return this.companiesPreferences
    },
  },
  actions: {
    async requestFreshCompaniesData() {
      const companiesResponse = await companyService.getAllCompanies()
      if (!isEqual(companiesResponse.data, this.companies)) {
        this.companies = companiesResponse.data
      }
      //     todo load preferences
    }
  }
})

export const useSysAdmSupplierStore = defineStore('sysAdmSupplier', {
  state: () => ({
    suppliers: [] as Supplier[],
    supplierPreferences: [] as SupplierPreferences[]
  }),
  getters: {
    getSuppliers(): Supplier[] {
      return this.suppliers
    },
    getSuppliersPreferences(): CompanyPreferences[] {
      return this.supplierPreferences
    },
  },
  actions: {
    async requestFreshSuppliersData() {
      const suppliersResponse = await supplierService.getAllSuppliers()
      if (!isEqual(suppliersResponse.data, this.suppliers)) {
        this.suppliers = suppliersResponse.data
      }
      //     todo load preferences
    }
  }
})

export const useUserStore = defineStore('user', {
  state: () => ({
    userDetails: null as UserDetails | null,
    user: null as User | null
  }),
  getters: {
    getUser(): User | null {
      return this.user
    },
    getUserDetails(): UserDetails | null {
      return this.userDetails
    },
    hasUser(): boolean {
      return this.userDetails !== null && this.user !== null
    },
    isAppUser(): boolean {
      return roleService.hasRole(this.userDetails, Role.User)
    },
    isOnlyAppUser(): boolean {
      return roleService.hasOnlyRole(this.userDetails, Role.User)
    },
    isEmployee(): boolean {
      return roleService.hasRole(this.userDetails, Role.Employee)
    },
    isCompanyAdmin(): boolean {
      return roleService.hasRole(this.userDetails, Role.CompanyAdmin)
    },
    isSupplier(): boolean {
      return roleService.hasRole(this.userDetails, Role.Supplier)
    },
    isSysAdmin(): boolean {
      return roleService.hasRole(this.userDetails, Role.SystemAdmin)
    }
  },
  actions: {
    clearUser() {
      this.userDetails = null;
      this.user = null;
    },
    async requestUserData() {
      try {
        const detailsResponse = await userService.getProfileDetails();
        this.userDetails = detailsResponse.data;
        const profileResponse = await userService.getProfile();
        this.user = profileResponse.data;
      } catch (error) {
        toastManager.showError("Profile not loaded",
          "Couldn't load profile data. Try again later.")
      }
    },
  }
})

export const useSysAdmUserStore = defineStore('sysAdmUser', {
  state: () => ({
    usersDetails: [] as UserDetails[],
    users: [] as User[]
  }),
  getters: {
    getUsers(): User[] {
      return this.users
    },
    getUsersDetails(): UserDetails[] {
      return this.usersDetails
    },
  },
  actions: {
    async requestFreshUsersData() {
      const usersResponse = await userService.getAllUsers()
      if (!isEqual(usersResponse.data, this.users)) {
        this.users = usersResponse.data
      }
      const usersDetailsResponse = await userService.getAllUsersDetails()
      if (!isEqual(usersDetailsResponse.data, this.users)) {
        this.usersDetails = usersDetailsResponse.data
      }
      //     todo load preferences
    }
  }
})

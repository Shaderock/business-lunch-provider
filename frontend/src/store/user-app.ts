// Utilities
import {defineStore} from 'pinia'
import authService from "@/services/AuthService";
import roleService from "@/services/RoleService";
import {UserDetails} from "@/models/UserDetails";
import {Role} from "@/models/Role";
import userService from "@/services/UserService";
import toastManager from "@/services/ToastManager";
import {User} from "@/models/User";
import {Invitation} from "@/models/Invitation";
import invitationService from "@/services/InvitationService";
import {AxiosResponse} from "axios";
import {PublicOrganizationDetails} from "@/models/PublicOrganizationDetails";
import {Utils} from "@/models/Utils";
import organizationService from "@/services/OrganizationService";
import {Supplier} from "@/models/Supplier";
import {PublicSupplierPreferences} from "@/models/PublicSupplierPreferences";
import {Duration} from "moment/moment";
import {OrderType} from "@/models/OrderType";
import {CategoryTag} from "@/models/CategoryTag";
import supplierService from "@/services/SupplierService";
import supplierPreferencesService from "@/services/SupplierPreferencesService";
import moment from "moment";

export const useAuthStore = defineStore('auth', {
  state: () => ({
    jwtToken: null as string | null
  }),
  getters: {
    hasToken(): boolean {
      return this.jwtToken !== null
    },
    isAuthenticated(): boolean {
      return this.hasToken && useProfileStore().hasUser
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
      const token: string | null = this.getToken();
      if (token) {
        return 'Bearer ' + token.toString()
      }
      return null
    },
    logout() {
      localStorage.removeItem("token")
      this.initializeToken()
      const userStore = useProfileStore();
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
      await useProfileStore().requestUserData()
      return Promise.resolve
    }
  }
})

export const useProfileStore = defineStore('user', {
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
    isOnlyEmployee(): boolean {
      return roleService.hasOnlyRole(this.userDetails, Role.Employee)
    },
    isUserAndEmployee(): boolean {
      return roleService.hasRoles(this.userDetails, [Role.User, Role.Employee])
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

export interface InvitingCompany {
  id: string
  name: string
  description: string
  email: string
  phone: string
  formattedCreatedAt: string | null
}

export const useInvitationStore = defineStore('userInvitations', {
  state: () => ({
    invitations: [] as Invitation[],
    companiesDetails: [] as PublicOrganizationDetails[]
  }),
  getters: {
    getInvitations(): Invitation[] {
      return this.invitations
    },
    getInvitingCompaniesDetails(): PublicOrganizationDetails[] {
      return this.companiesDetails;
    },
    getInvitingCompanies(): InvitingCompany[] {
      return this.companiesDetails.map(company => {
        const invitation = this.invitations.find(invitation => invitation.companyId === company.id);
        const formattedCreatedAt = invitation ? invitation.formattedCreatedAt : null;
        return {
          id: company.id,
          name: company.name,
          description: company.description,
          email: company.email,
          phone: company.phone,
          formattedCreatedAt,
        };
      });
    },
  },
  actions: {
    async accept(companyId: string) {
      await invitationService.acceptInvitation(companyId)
    },
    async decline(companyId: string) {
      try {
        await invitationService.declineInvitation(companyId)
        await this.requestFreshInvitationData()
      } catch (error) {
        console.log("Error during invitation decline")
      }
    },
    async requestFreshInvitationData() {
      this.invitations = []
      this.companiesDetails = []

      const invitationsResponse: AxiosResponse<Invitation[]> =
        await invitationService.getAllUserInvitation()
      this.invitations = invitationsResponse.data

      const organizationsResponse: AxiosResponse<PublicOrganizationDetails[]> =
        await organizationService.getAllUserInvitingOrganizations()
      this.companiesDetails = organizationsResponse.data

      this.invitations = this.invitations.map(invitation => {
        return {
          ...invitation,
          formattedCreatedAt: Utils.dateToDateString(invitation.createdAt)
        };
      });
    }
  }
})

export interface WorkingSupplier {
  // details
  name: string
  description: string
  email: string
  phone: string

  // supplier
  supplierId: string
  websiteUrl: string
  menuUrl: string

  // preferences
  requestOffset: Duration
  deliveryPeriodStartTime: Date
  deliveryPeriodEndTime: Date
  minimumOrdersPerCompanyRequest: number
  minimumCategoriesForEmployeeOrder: number
  orderType: OrderType
  pricesForCategoriesIds: string[] | null
  categoriesTags: CategoryTag[]
}

export const userWorkingSuppliersStore = defineStore('publicSuppliers', {
  state: () => ({
    publicSuppliers: [] as Supplier[],
    publicSuppliersDetails: [] as PublicOrganizationDetails[],
    publicSuppliersPreferences: [] as PublicSupplierPreferences[],
    suppliersLimit: 10 as number
  }),
  getters: {
    getSuppliers(): Supplier[] {
      return this.publicSuppliers
    },
    getPreferences(): PublicSupplierPreferences[] {
      return this.publicSuppliersPreferences
    },
    getDetails(): PublicOrganizationDetails[] {
      return this.publicSuppliersDetails
    },
    getWorkingSuppliers(): WorkingSupplier[] {
      return this.publicSuppliers.map(supplier => {
        const details = this.publicSuppliersDetails.find(d => d.id === supplier.organizationDetailsId)
        const preferences = this.publicSuppliersPreferences.find(p => p.id === supplier.preferencesId)
        return {

          name: details?.name ?? '',
          description: details?.description ?? '',
          email: details?.email ?? '',
          phone: details?.phone ?? '',

          supplierId: supplier.id ?? '',
          websiteUrl: supplier.websiteUrl,
          menuUrl: supplier.menuUrl,

          requestOffset: preferences?.requestOffset ?? moment.duration(),
          deliveryPeriodStartTime: preferences?.deliveryPeriodStartTime ?? new Date(),
          deliveryPeriodEndTime: preferences?.deliveryPeriodEndTime ?? new Date(),
          minimumOrdersPerCompanyRequest: preferences?.minimumOrdersPerCompanyRequest ?? 0,
          minimumCategoriesForEmployeeOrder: preferences?.minimumCategoriesForEmployeeOrder ?? 0,
          orderType: preferences?.orderType ?? OrderType.UnlimitedOptions,
          pricesForCategoriesIds: preferences?.pricesForCategoriesIds ?? [],
          categoriesTags: preferences?.categoriesTags ?? []
        }
      })
    },
    getWorkingSuppliersLimited(): WorkingSupplier[] {
      return this.getWorkingSuppliers.slice(0, this.suppliersLimit)
    }
  },
  actions: {
    async requestFreshData() {
      this.publicSuppliers = []
      this.publicSuppliersDetails = []
      this.publicSuppliersPreferences = []

      const suppliersResponse: AxiosResponse<Supplier[]> = await supplierService.anonymousRequestForSuppliers()
      this.publicSuppliers = suppliersResponse.data

      const detailsResponse: AxiosResponse<PublicOrganizationDetails[]> =
        await organizationService.anonymousRequestForDetails()
      this.publicSuppliersDetails = detailsResponse.data

      const preferencesResponse: AxiosResponse<PublicSupplierPreferences[]> =
        await supplierPreferencesService.anonymousRequestForPreferences()
      this.publicSuppliersPreferences = preferencesResponse.data
    },
    async requestFreshDataIfNothingCached() {
      if (this.publicSuppliers.length === 0)
        await this.requestFreshData()
    },
    incrementLimit() {
      this.suppliersLimit += 10
    }
  }
})

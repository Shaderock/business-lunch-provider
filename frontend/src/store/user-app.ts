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
import {Category} from "@/models/Category";
import {Option} from "@/models/Option";
import categoryService from "@/services/CategoryService";
import optionService from "@/services/OptionService";
import {Subscription} from "@/models/Subscription";
import subscriptionService from "@/services/SubscriptionService";

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
    hasRole(role: Role): boolean {
      return roleService.hasRole(this.userDetails, role)
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
  logo: string | null
  logoThumbnail: string
}

interface InvitingCompanyLogo {
  companyId: string
  logo: string
  logoThumbnail: string
}

export const useInvitationStore = defineStore('userInvitations', {
  state: () => ({
    invitations: [] as Invitation[],
    companiesDetails: [] as PublicOrganizationDetails[],
    invitingCompaniesLogos: [] as InvitingCompanyLogo[]
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
        const invitation = this.invitations.find(invitation => invitation.companyId === company.id)
        const formattedCreatedAt = invitation ? invitation.formattedCreatedAt : null
        const companyWithLogo = this.invitingCompaniesLogos.find(companyWithLogos => companyWithLogos.companyId === company.id);

        return {
          id: company.id,
          name: company.name,
          description: company.description,
          email: company.email,
          phone: company.phone,
          logo: companyWithLogo?.logo || '',
          logoThumbnail: companyWithLogo?.logoThumbnail || '',
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
        this.invitations = this.invitations.filter(invitation => invitation.companyId !== companyId)
        this.companiesDetails = this.companiesDetails.filter(details => details.id !== companyId)
        this.invitingCompaniesLogos = this.invitingCompaniesLogos.filter(company => company.companyId !== companyId)
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
          formattedCreatedAt: Utils.formatDateWithoutTimeWithSlashes(invitation.createdAt)
        };
      });

      this.invitingCompaniesLogos = await Promise.all(this.companiesDetails.map(async company => {
        const logoThumbnail = await organizationService.requestInvitingCompanyLogo(company.id, true);
        return {
          companyId: company.id,
          logo: '',
          logoThumbnail
        }
      }))
    },
    async requestCompaniesLogos() {
      this.invitingCompaniesLogos = await Promise.all(this.invitingCompaniesLogos.map(async company => {
        const logo = await organizationService.requestInvitingCompanyLogo(company.companyId, false);
        return {
          companyId: company.companyId,
          logo: logo,
          logoThumbnail: company.logoThumbnail
        }
      }))
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
  workDayStart: Date
  workDayEnd: Date
  minimumOrdersPerCompanyRequest: number
  minimumCategoriesForEmployeeOrder: number
  orderType: OrderType
  pricesForCategoriesIds: string[] | null
  categoriesTags: CategoryTag[]
}

export const useWorkingSuppliersStore = defineStore('publicSuppliers', {
  state: () => ({
    publicSuppliers: [] as Supplier[],
    publicSuppliersDetails: [] as PublicOrganizationDetails[],
    publicSuppliersPreferences: [] as PublicSupplierPreferences[],
    suppliersLimit: 12 as number,

    //filters
    filterShowClosed: null as boolean | null,

    filterShowByOrderType: false,
    filterOrderTypes: [] as OrderType[],

    filterShowByMinimumOrders: false,
    // todo create filters
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
          workDayStart: preferences?.workDayStart ?? new Date(),
          workDayEnd: preferences?.workDayEnd ?? new Date(),
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
    },
    getWorkingsSuppliersLimitedFiltered(): WorkingSupplier[] {
      return this.getWorkingSuppliersLimited
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
        await organizationService.anonymousRequestForAllDetails()
      this.publicSuppliersDetails = detailsResponse.data

      const preferencesResponse: AxiosResponse<PublicSupplierPreferences[]> =
        await supplierPreferencesService.anonymousRequestForAllPreferences()
      this.publicSuppliersPreferences = preferencesResponse.data
    },
    async requestFreshDataIfNothingCached() {
      if (this.publicSuppliers.length === 0)
        await this.requestFreshData()
    },
    incrementLimit() {
      this.suppliersLimit++
    }
  }
})

export interface CategoryOptions {
  category: Category
  options: Option[]
}

export const usePublicSupplierStore = defineStore('publicSupplierProfile', {
  state: () => ({
    currentSupplierName: '' as string,
    supplier: null as Supplier | null,
    publicSuppliersDetails: null as PublicOrganizationDetails | null,
    publicSuppliersPreferences: null as PublicSupplierPreferences | null,
    subscriptions: [] as Subscription [],

    categoriesOptions: [] as CategoryOptions[],
    currentCategoryOptions: null as CategoryOptions | null,

    optionsLimit: 12 as number,
    defaultOptionsLimit: 12 as number
  }),
  getters: {
    getCurrentSupplierName(): string {
      return this.currentSupplierName
    },
    isSupplierFound(): boolean {
      if (!this.supplier) return false
      return this.supplier.id !== '';
    },
    getSupplier(): Supplier | null {
      return this.supplier
    },
    getDetails(): PublicOrganizationDetails | null {
      return this.publicSuppliersDetails
    },
    getPreferences(): PublicSupplierPreferences | null {
      return this.publicSuppliersPreferences
    },
    getCurrentCategory(): CategoryOptions | null {
      return this.currentCategoryOptions
    },
    getCategoriesOptions(): CategoryOptions[] {
      return this.categoriesOptions
    },
    getOptionsLimited(): Option[] {
      return this.categoriesOptions
      .filter(c => c.category.id === this.getCurrentCategory?.category?.id)
      .flatMap(c => {
        return c.options
      })
      .slice(0, this.optionsLimit)
    },
    isCompanySubscribed(): boolean {
      if (useProfileStore().isEmployee) {
        return this.subscriptions.filter(s => s.supplierId === this.getSupplier?.id).length > 0
      } else {
        return false
      }
    }
  },
  actions: {
    clearData(): void {
      this.supplier = null
      this.publicSuppliersDetails = null
      this.publicSuppliersPreferences = null
      this.categoriesOptions = []
      this.currentCategoryOptions = null
    },
    setCurrentSupplierName(name: string): void {
      this.currentSupplierName = name
    },
    async setCurrentCategoryAndFetch(categoryOption: CategoryOptions): Promise<void> {
      const categoryOptions =
        this.categoriesOptions.find(c => c.category.id === categoryOption.category.id)
      this.currentCategoryOptions = categoryOptions || {
        category: categoryOption.category,
        options: []
      }
      this.optionsLimit = this.defaultOptionsLimit
      if (this.currentCategoryOptions.options.length === 0) {
        const optionsResponse = await optionService.requestOptionsForCategory(this.currentCategoryOptions.category.id)
        this.currentCategoryOptions.options = optionsResponse.data

        const optionsWithoutPhotoAmount: number =
          this.currentCategoryOptions.options.filter(o => !o.hasPhoto).length
        this.optionsLimit += optionsWithoutPhotoAmount
      }
    },
    incrementLimit(): void {
      this.optionsLimit++
    },
    async requestFreshData(): Promise<void> {
      this.categoriesOptions = []

      const supplierResponse = await supplierService.requestSupplier(this.currentSupplierName)
      this.supplier = supplierResponse.data
      const organizationResponse = await organizationService.requestDetailsForSupplier(this.currentSupplierName)
      this.publicSuppliersDetails = organizationResponse.data
      const preferencesResponse = await supplierPreferencesService.requestPreferencesForSupplier(this.currentSupplierName)
      this.publicSuppliersPreferences = preferencesResponse.data
      const categoriesResponse = await categoryService.requestCategoriesForSupplier(this.currentSupplierName)

      for (const category of categoriesResponse.data) {
        this.categoriesOptions.push({category: category, options: []})
      }

      if (this.categoriesOptions.length > 0) {
        await this.setCurrentCategoryAndFetch(this.categoriesOptions[0])
      }

      if (useProfileStore().isEmployee) {
        const subscriptionsResponse: AxiosResponse<Subscription[]> =
          await subscriptionService.requestCompanySubscriptions()
        this.subscriptions = subscriptionsResponse.data
      }
    },
    async requestFreshDataIfEmpty() {
      if (this.currentSupplierName !== '' && this.isSupplierFound) {
        await this.requestFreshData()
      }
    }
  }
})

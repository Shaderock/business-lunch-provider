import {defineStore} from "pinia";
import {SupplierPreferences} from "@/models/SupplierPreferences";
import supplierPreferencesService from "@/services/SupplierPreferencesService";
import {AxiosResponse} from "axios";
import moment, {Duration} from "moment";
import {Supplier} from "@/models/Supplier";
import supplierService from "@/services/SupplierService";
import {OrderType} from "@/models/OrderType";
import {OrganizationDetails} from "@/models/OrganizationDetails";
import {Subscription} from "@/models/Subscription";
import {SubscriptionStatus} from "@/models/SubscriptionStatus";
import {Utils} from "@/models/Utils";
import subscriptionService from "@/services/SubscriptionService";
import organizationService from "@/services/OrganizationService";
import {PublicCompanyPreferences} from "@/models/PublicCompanyPreferences";
import {Company} from "@/models/Company";
import companyService from "@/services/CompanyService";
import companyPreferencesService from "@/services/CompanyPreferencesService";

export const useSupAdmSupPrefStore = defineStore('supplierAdminSupplierPreferences', {
  state: () => ({
    preferences: new SupplierPreferences(null,
      null,
      moment.duration(null),
      new Date(),
      new Date(),
      0,
      0,
      OrderType.OnlyOneOptionPerCategory,
      [],
      null,
      [])
  }),
  getters: {
    getPreferences(): SupplierPreferences {
      return this.preferences
    },
    getRequestOffset(): Duration | null {
      return moment.duration(this.preferences.requestOffset)
    },
    getStartTime(): Date | null {
      if (this.preferences.workDayStart !== null)
        return moment(this.preferences?.workDayStart, "HH:mm:ss").toDate()
      else
        return null
    },
    getEndTime(): Date | null {
      if (this.preferences.workDayEnd !== null)
        return moment(this.preferences.workDayEnd, "HH:mm:ss").toDate()
      else
        return null
    },
    arePreferencesCompleted(): boolean {
      if (!this.preferences) return false
      return !(!this.preferences.requestOffset ||
        !this.preferences.workDayStart ||
        !this.preferences.workDayEnd ||
        !this.preferences.minimumOrdersPerCompanyRequest ||
        !this.preferences.minimumCategoriesForEmployeeOrder ||
        !this.preferences.orderType ||
        !this.preferences.workDayEnd ||
        !this.preferences.categoriesTags ||
        this.preferences.categoriesTags.length == 0);
    },
    hasPreferences(): boolean {
      return this.preferences !== null;
    },
  },
  actions: {
    async requestFreshPreferencesData() {
      const response: AxiosResponse<SupplierPreferences> = await supplierPreferencesService.getSupplierPreferences()
      this.preferences = response.data
    }
  }
})

export const useSupAdmSupStore = defineStore('supplierAdminSupplier', {
  state: () => ({
    supplier: new Supplier("", "", "", "", false, 0)
  }),
  getters: {
    getSupplier(): Supplier {
      return this.supplier
    },
    getPublic(): boolean {
      return this.supplier.isPublic || false
    }
  },
  actions: {
    async update(websiteUrl: string, menuUrl: string) {
      const updatedSupplier = {
        ...this.supplier, websiteUrl: websiteUrl, menuUrl: menuUrl
      };

      await supplierService.updateSupplier(updatedSupplier)
      await this.requestFreshSupplierData()
    },
    async updatePublic(isPublic: boolean) {
      const updatedSupplier = {
        ...this.supplier, isPublic: isPublic
      };

      await supplierService.updateSupplier(updatedSupplier)
      await this.requestFreshSupplierData()
    },

    async requestFreshSupplierData() {
      const response: AxiosResponse<Supplier> = await supplierService.getUserSupplier()
      this.supplier = response.data
    }
  }
})


export interface SubscriberCompany {
  // company
  companyId: string

  // details
  name: string
  phone: string
  email: string

  // preferences
  deliveryAddress: string

  // subscription
  subscriptionId: string
  subscriptionStatus: SubscriptionStatus
  subscriptionDate: string
}

export const useSubscribersCompaniesStore = defineStore('supplierAdminSubcribersCompanies', {
  state: () => ({
    companies: [] as Company[],
    companiesPreferences: [] as PublicCompanyPreferences[],
    companiesDetails: [] as OrganizationDetails[],
    subscriptions: [] as Subscription[]
  }),
  getters: {
    getCompanies(): Company[] {
      return this.companies
    },
    getCompaniesPreferences(): PublicCompanyPreferences[] {
      return this.companiesPreferences
    },
    getCompaniesDetails(): OrganizationDetails[] {
      return this.companiesDetails
    },
    getSubscribersCompanies(): SubscriberCompany[] {
      return this.companies.map(company => {
        const preferences = this.companiesPreferences.find(p => p.id === company.preferencesId)
        const details = this.companiesDetails.find(d => d.id === company.organizationDetailsId)
        const subscription = this.subscriptions.find(s => s.companyId === company.id)

        return {
          name: details?.name ?? '',
          email: details?.email ?? '',
          phone: details?.phone ?? '',

          companyId: company.id,

          deliveryAddress: preferences?.deliveryAddress ?? '',

          subscriptionId: subscription?.id ?? '',
          subscriptionStatus: subscription?.subscriptionStatus ?? SubscriptionStatus.Pending,
          subscriptionDate: subscription?.createdAt ? Utils.dateToDateString(subscription.createdAt) : ''
        }
      })
    }
  },
  actions: {
    async acceptSubscription(companyId: string) {
      try {
        console.log("store:" + companyId)
        await subscriptionService.acceptSubscription(companyId);
        const subscription = this.subscriptions.find(subscription => subscription.companyId === companyId);
        if (subscription)
          subscription.subscriptionStatus = SubscriptionStatus.Accepted
      } catch (error) {
        console.log("Couldn't accept a subscription")
      }
    },
    async declineSubscription(companyId: string) {
      try {
        await subscriptionService.declineSubscription(companyId)
        this.companies = this.companies.filter(s => s.id !== companyId)
      } catch (error) {
        console.log("Couldn't decline a subscription")
      }
    },
    async requestFreshData() {
      const companiesResponse: AxiosResponse<Company[]> =
        await companyService.requestSupplierSubscribers()
      this.companies = companiesResponse.data

      const preferencesResponse: AxiosResponse<PublicCompanyPreferences[]> =
        await companyPreferencesService.requestSubscribersCompaniesPreferences()
      this.companiesPreferences = preferencesResponse.data

      const detailsResponse: AxiosResponse<OrganizationDetails[]> =
        await organizationService.requestSubscribersCompaniesDetails()
      this.companiesDetails = detailsResponse.data

      const subscriptionsResponse: AxiosResponse<Subscription[]> =
        await subscriptionService.requestSupplierSubscribers()
      this.subscriptions = subscriptionsResponse.data
    }
  }
})

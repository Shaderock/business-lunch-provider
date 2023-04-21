import {defineStore} from "pinia";
import {Company} from "@/models/Company";
import {CompanyPreferences} from "@/models/CompanyPreferences";
import companyService from "@/services/CompanyService";
import moment from "moment/moment";
import {AxiosResponse} from "axios";
import companyPreferencesService from "@/services/CompanyPreferencesService";

export const useCompanyAdmCompanyStore = defineStore('company', {
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
    async requestFreshOrganizationData() {
      const response = await companyService.getUserCompany()
      this.company = response.data
      //     todo load preferences
    }
  }
})

export const useCompAdmCompPrefStore = defineStore('companyAdminCompanyPreferences', {
  state: () => ({
    companyPreferences: null as CompanyPreferences | null
  }),
  getters: {
    getPreferences(): CompanyPreferences | null {
      return this.companyPreferences
    },
    getDeliveryTime(): Date | null {
      if (this.companyPreferences?.deliverAt !== null)
        return moment(this.companyPreferences?.deliverAt, "HH:mm:ss").toDate()
      else
        return null
    },
    hasPreferences(): boolean {
      return this.companyPreferences !== null;
    },
  },
  actions: {
    clearPreferences() {
      this.companyPreferences = null
    },
    async requestFreshPreferencesData() {
      const response: AxiosResponse<CompanyPreferences> = await companyPreferencesService.getCompanyPreferences()
      this.companyPreferences = response.data
    }
  }
})

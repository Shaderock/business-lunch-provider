import {defineStore} from "pinia";
import {Company} from "../models/Company";
import {CompanyPreferences} from "../models/CompanyPreferences";
import {CompanyService} from "../services/CompanyService";

const companyService: CompanyService = new CompanyService()
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

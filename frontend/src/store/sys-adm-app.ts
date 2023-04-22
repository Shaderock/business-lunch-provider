import {defineStore} from "pinia";
import {OrganizationDetails} from "@/models/OrganizationDetails";
import {Supplier} from "@/models/Supplier";
import {SupplierPreferences} from "@/models/SupplierPreferences";
import {Company} from "@/models/Company";
import {CompanyPreferences} from "@/models/CompanyPreferences";
import organizationService from "@/services/OrganizationService";
import {isEqual} from "lodash";
import supplierService from "@/services/SupplierService";
import companyService from "@/services/CompanyService";
import {UserDetails} from "@/models/UserDetails";
import {User} from "@/models/User";
import userService from "@/services/UserService";

export const useSysAdmOrganizationStore = defineStore('sysAdmOrganization', {
  state: () => ({
    organizationsDetails: [] as OrganizationDetails[],
    suppliers: [] as Supplier[],
    preferences: [] as SupplierPreferences[],
    companies: [] as Company[],
    companiesPreferences: [] as CompanyPreferences[]
  }),
  getters: {
    getOrganizationsDetails(): OrganizationDetails[] {
      return this.organizationsDetails
    },
    getSuppliers(): Supplier[] {
      return this.suppliers
    },
    getSuppliersPreferences(): SupplierPreferences[] {
      return this.preferences
    },
    getCompanies(): Company[] {
      return this.companies
    },
    getCompaniesPreferences(): CompanyPreferences[] {
      return this.companiesPreferences
    }
  },
  actions: {
    async requestFreshOrganizationsData() {
      const organizationsResponse = await organizationService.getAllOrganizations()
      if (!isEqual(organizationsResponse.data, this.organizationsDetails)) {
        this.organizationsDetails = organizationsResponse.data
      }
    },
    async requestFreshSuppliersData() {
      await this.requestFreshOrganizationsData();
      const suppliersResponse = await supplierService.getAllSuppliers()
      if (!isEqual(suppliersResponse.data, this.suppliers)) {
        this.suppliers = suppliersResponse.data
      }
      //     todo load preferences
    },
    async requestFreshCompaniesData() {
      await this.requestFreshOrganizationsData();
      const companiesResponse = await companyService.getAllCompanies()
      if (!isEqual(companiesResponse.data, this.companies)) {
        this.companies = companiesResponse.data
      }
      //     todo load preferences
    }

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

import {defineStore} from "pinia";
import {Company} from "@/models/Company";
import {CompanyPreferences} from "@/models/CompanyPreferences";
import companyService from "@/services/CompanyService";
import moment from "moment/moment";
import {AxiosResponse} from "axios";
import companyPreferencesService from "@/services/CompanyPreferencesService";
import {UserDetails} from "@/models/UserDetails";
import userService from "@/services/UserService";
import {Role} from "@/models/Role";
import {Employee} from "@/models/Employee";
import {Invitation} from "@/models/Invitation";
import invitationService from "@/services/InvitationService";

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

export const useInvitationStore = defineStore('companyAdminInvitations', {
  state: () => ({
    invitations: [] as Invitation[]
  }),
  getters: {
    getInvitations(): Invitation[] {
      return this.invitations
    },
  },
  actions: {
    clearInvitations() {
      this.invitations = []
    },
    async requestFreshInvitationData() {
      const response: AxiosResponse<Invitation[]> = await invitationService.getAllCompAdmInvitations()
      this.invitations = response.data
    }
  }
})

export const useCompAdmUserStore = defineStore('companyAdminEmployees', {
  state: () => ({
    employees: [] as UserDetails[]
  }),
  getters: {
    getEmployeesDetails(): UserDetails[] {
      return this.employees
    },
    getEmployees(): Employee[] {
      return this.employees.map(employee => ({
        ...employee,
        isAdmin: employee.roles.includes(Role.CompanyAdmin)
      }))
    }
  },
  actions: {
    clearEmployees() {
      this.employees = []
    },
    async removeEmployeeByEmail(email: string) {
      try {
        await userService.deleteEmployee(email)
        this.employees = this.employees.filter(employee => employee.email !== email)
      } catch (error) {
        console.log("Couldn't remove employee")
      }
    },
    async grantAdminRights(email: string) {
      try {
        await userService.grantAdmin(email)
        const employee = this.employees.find(employee => employee.email === email);
        console.log(employee)
        if (employee) {
          employee.roles = [Role.Employee, Role.CompanyAdmin]
        }
      } catch (error) {
        console.log("Couldn't grant rights")
      }
    },

    async revokeAdminRights(email: string) {
      try {
        await userService.revokeAdmin(email)
        const employee = this.employees.find(employee => employee.email === email);
        console.log(employee)
        if (employee) {
          employee.roles = [Role.Employee]
        }
      } catch (error) {
        console.log("Couldn't revoke rights")
      }
    },
    async requestFreshEmployeesData() {
      const response: AxiosResponse<UserDetails[]> = await userService.requestEmployeesDetails()
      this.employees = response.data
    }
  }
})



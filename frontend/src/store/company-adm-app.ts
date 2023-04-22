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
import {Invitation} from "@/models/Invitation";
import invitationService from "@/services/InvitationService";
import toastManager from "@/services/ToastManager";
import {Utils} from "@/models/Utils";
import {CompanyDiscountType} from "@/models/CompanyDiscountType";

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
    companyPreferences: new CompanyPreferences(
      null,
      null,
      CompanyDiscountType.SpecificAmountFirst,
      0,
      0,
      0,
      0,
      new Date(),
      ""
    )
  }),
  getters: {
    getPreferences(): CompanyPreferences {
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
    async requestFreshPreferencesData() {
      const response: AxiosResponse<CompanyPreferences> = await companyPreferencesService.getCompanyPreferences()
      this.companyPreferences = response.data
    }
  }
})

export const useCompAdmInvitationStore = defineStore('companyAdminInvitations', {
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
    async send(userEmail: string) {
      try {
        await invitationService.send(userEmail)
        await this.requestFreshInvitationData()
        toastManager.showInfo("Invited!", `Invitation for user ${userEmail} has been sent`)
      } catch (error) {
        console.log("Error during user invitation")
      }
    },
    async dismissInvitation(userEmail: string) {
      try {
        await invitationService.dismissInvitation(userEmail)
        this.invitations = this.invitations.filter(invitation => invitation.userEmail !== userEmail)
      } catch (error) {
        console.log("Couldn't dismiss invitation")
      }
    },
    async requestFreshInvitationData() {
      this.clearInvitations()
      const response: AxiosResponse<Invitation[]> = await invitationService.getAllCompAdmInvitations()
      this.invitations = response.data
      this.invitations = this.invitations.map(invitation => {
        return {
          ...invitation,
          formattedCreatedAt: Utils.dateToDateString(invitation.createdAt)
        };
      });

    }
  }
})

export interface Employee {
  email: string
  firstName: string
  lastName: string
  isAdmin: boolean
}

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



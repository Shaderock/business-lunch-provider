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
import {OrderType} from "@/models/OrderType";
import {SubscriptionStatus} from "@/models/SubscriptionStatus";
import {Supplier} from "@/models/Supplier";
import {PublicSupplierPreferences} from "@/models/PublicSupplierPreferences";
import {OrganizationDetails} from "@/models/OrganizationDetails";
import {Subscription} from "@/models/Subscription";
import subscriptionService from "@/services/SubscriptionService";
import supplierService from "@/services/SupplierService";
import organizationService from "@/services/OrganizationService";
import supplierPreferencesService from "@/services/SupplierPreferencesService";
import {useWorkingSuppliersStore, WorkingSupplier} from "@/store/user-app";
import {EmployeeOrder} from "@/models/EmployeeOrder";
import employeeOrderService from "@/services/EmployeeOrderService";
import {EmployeeOrderValidation} from "@/models/EmployeeOrderValidation";
import {EmployeeOrderStatus} from "@/models/EmployeeOrderStatus";
import {Option} from "@/models/Option";
import optionService from "@/services/OptionService";
import {CompanyOrderValidation} from "@/models/CompanyOrderValidation";
import companyOrderService from "@/services/CompanyOrderService";
import {CompanyOrder} from "@/models/CompanyOrder";
import {CompanyOrderStatus} from "@/models/CompanyOrderStatus";


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
    },
    async requestDataIfEmpty() {
      if (!this.getPreferences.id) {
        await this.requestFreshPreferencesData()
      }
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
          formattedCreatedAt: Utils.formatDateWithoutTimeWithSlashes(invitation.createdAt)
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

export interface SubscriptionSupplier {
  // supplier
  supplierId: string
  menuUrl: string

  // details
  name: string
  phone: string
  email: string

  // preferences
  orderType: OrderType
  minimumOrdersPerCompanyRequest: number
  minimumCategoriesForEmployeeOrder: number

  // subscription
  subscriptionId: string
  subscriptionStatus: SubscriptionStatus
  subscriptionDate: string
}

export const useCompAdmSubscriptionStore = defineStore('companyAdminSubscriptionSuppliers', {
  state: () => ({
    suppliers: [] as Supplier[],
    suppliersPreferences: [] as PublicSupplierPreferences[],
    suppliersDetails: [] as OrganizationDetails[],
    subscriptions: [] as Subscription[]
  }),
  getters: {
    getSuppliers(): Supplier[] {
      return this.suppliers
    },
    getSuppliersPreferences(): PublicSupplierPreferences[] {
      return this.suppliersPreferences
    },
    getSuppliersDetails(): OrganizationDetails[] {
      return this.suppliersDetails
    },
    getSubscriptionSuppliers(): SubscriptionSupplier[] {
      return this.suppliers.map(supplier => {
        const preferences = this.suppliersPreferences.find(p => p.id === supplier.preferencesId)
        const details = this.suppliersDetails.find(d => d.id === supplier.organizationDetailsId)
        const subscription = this.subscriptions.find(s => s.supplierId === supplier.id)
        return {
          name: details?.name ?? '',
          email: details?.email ?? '',
          phone: details?.phone ?? '',

          supplierId: supplier.id ?? '',
          menuUrl: supplier.menuUrl,

          minimumOrdersPerCompanyRequest: preferences?.minimumOrdersPerCompanyRequest ?? 0,
          minimumCategoriesForEmployeeOrder: preferences?.minimumCategoriesForEmployeeOrder ?? 0,
          orderType: preferences?.orderType ?? OrderType.UnlimitedOptions,

          subscriptionId: subscription?.id ?? '',
          subscriptionStatus: subscription?.subscriptionStatus ?? SubscriptionStatus.Pending,
          subscriptionDate: subscription?.createdAt ? Utils.formatDateWithoutTimeWithSlashes(subscription.createdAt) : ''
        }
      })
    },
    getValidSuppliersDetailsListForSubscription(): WorkingSupplier[] {
      return useWorkingSuppliersStore().getWorkingSuppliers.filter(
        (publicSupplier: WorkingSupplier) =>
          !this.suppliers.some(supplier => supplier.id === publicSupplier.supplierId)
      )
    },
    getValidSuppliersDetailsListNameForSubscription(): string[] {
      return this.getValidSuppliersDetailsListForSubscription.map(supplier => supplier.name)
    },
  },
  actions: {
    async subscribe(supplierNames: string[]) {
      try {
        for (const supplierName of supplierNames) {
          const chosenSupplier: WorkingSupplier | undefined = this.getValidSuppliersDetailsListForSubscription.find(s => s.name === supplierName);

          if (chosenSupplier) {
            await subscriptionService.subscribe(chosenSupplier.supplierId)
          }
        }
        // await this.requestFreshData()
      } catch (error) {
        console.log("Couldn't subscribe to a supplier")
      }
    },
    async unsubscribe(supplierId: string) {
      try {
        await subscriptionService.unsubscribe(supplierId)
        this.suppliers = this.suppliers.filter(s => s.id !== supplierId)
      } catch (error) {
        console.log("Couldn't unsubscribe from a supplier")
      }
    },
    async requestFreshData() {
      const suppliersResponse: AxiosResponse<Supplier[]> =
        await supplierService.requestSubscriptionSuppliers()
      this.suppliers = suppliersResponse.data

      const preferencesResponse: AxiosResponse<PublicSupplierPreferences[]> =
        await supplierPreferencesService.requestSubscriptionSuppliersPreferences()
      this.suppliersPreferences = preferencesResponse.data

      const detailsResponse: AxiosResponse<OrganizationDetails[]> =
        await organizationService.requestSubscriptionSuppliersDetails()
      this.suppliersDetails = detailsResponse.data

      const subscriptionsResponse: AxiosResponse<Subscription[]> =
        await subscriptionService.requestCompanySubscriptions()
      this.subscriptions = subscriptionsResponse.data

      await useWorkingSuppliersStore().requestFreshDataIfNothingCached()
    },
    async requestDataIfEmpty() {
      if (this.suppliers.length == 0 || this.subscriptions.length == 0)
        await this.requestFreshData()
    }
  }
})

/*<template>
  <v-select
    v-model="selectedCategories"
    :items="categories"
    item-text="name"
    item-value="id"
    label="Select categories"
    multiple
  ></v-select>
</template>

<script lang="ts">
import Vue from 'vue';
import { Component } from 'vue-property-decorator';

interface Category {
  id: string;
  name: string;
}

@Component
export default class MyComponent extends Vue {
  selectedCategories: string[] = [];
  categories: Category[] = [
    { id: '1', name: 'Category 1' },
    { id: '2', name: 'Category 2' },
    { id: '3', name: 'Category 3' },
  ];
}
</script>

In this example, we have an array of Category objects called categories, which we pass to the v-select component using the items prop. We also specify that the text displayed for each item should be the value of its name property (using the item-text prop), and that the value of each item should be its id property (using the item-value prop).

When a user selects one or more items from the list, the v-model (selectedCategories) will be updated with an array of the selected items’ ids.
*/

export interface EmployeeOrdersInfo {
  employeeDetails: UserDetails
  ordersExtended: EmployeeOrderExtended[]
}

export interface EmployeeOrderExtended {
  order: EmployeeOrder
  validation: EmployeeOrderValidation
  supplierDetails: OrganizationDetails
  supplier: Supplier
  options: Option[]
}

export interface SupplierOptions {
  supplier: Supplier
  options: Option[]
}

export interface EmployeesOrdersTableRecord {
  orderId: string,
  supplierId: string,
  email: string
  supplierDefaultPrice: number
  supplierDiscount: number
  companyDiscount: number
  finalPrice: number
  options: number
  status: EmployeeOrderStatus
  isValid: boolean
}

export const useCompAdmEmpOrderStore = defineStore('companyAdminEmployeeOrders', {
  state: () => ({
    // todo use item-value="id" prop on select to iterate on select with duplicated values
    // todo when sending order, filter ones with pending admin confirm status
    selectedDate: Utils.formatDateWithoutTimeWithDashes(new Date),
    selectedTime: Utils.dateToTimeAsStringWithoutSeconds(new Date()),
    selectedSupplierDetails: null as OrganizationDetails | null,
    employeesOrders: [] as EmployeeOrder[],
    employeesOrdersValidations: [] as EmployeeOrderValidation[],
    suppliersOptions: [] as SupplierOptions[],
    companyOrderValidation: new CompanyOrderValidation(false, []),
  }),
  getters: {
    getSelectedDate(): string {
      return this.selectedDate
    },
    getSelectedTime(): string {
      return this.selectedTime
    },
    getSelectedDateTime(): Date {
      return Utils.dateAsStrAndTimeAsStrToDate(this.getSelectedDate, this.getSelectedTime)
    },
    getCompanyOrderValidation(): CompanyOrderValidation {
      return this.companyOrderValidation
    },
    getSelectedSupplier(): Supplier | undefined {
      return this.getSuppliersOptions
      .map(so => so.supplier)
      .find(s => s.organizationDetailsId === this.getSelectedSupplierDetails?.id)
    },
    getSelectedSupplierPreferences(): PublicSupplierPreferences | undefined {
      return useCompAdmSubscriptionStore().getSuppliersPreferences
      .find(p => this.getSelectedSupplier?.preferencesId === p.id)
    },
    getSelectedSupplierDetails(): OrganizationDetails | null {
      return this.selectedSupplierDetails
    },
    getSuppliersDetails(): OrganizationDetails[] {
      const suppliers = this.getEmployeesOrdersInfos
      .flatMap(eoi => eoi.ordersExtended)
      .map(o => o.supplier);

      const uniqueSuppliers = new Map(suppliers.map(s => [s.id, s]));

      return useCompAdmSubscriptionStore().suppliersDetails
      .filter(sd => Array.from(uniqueSuppliers.values())
      .some(s => s.organizationDetailsId === sd.id));
    },
    getEmployeesOrders(): EmployeeOrder[] {
      return this.employeesOrders
    },
    getEmployeesOrdersValidations(): EmployeeOrderValidation[] {
      return this.employeesOrdersValidations
    },
    getSuppliersOptions(): SupplierOptions[] {
      return this.suppliersOptions
    },
    getEmployeesOrdersInfos(): EmployeeOrdersInfo[] {
      return useCompAdmUserStore().getEmployeesDetails
      .map(empDetails => {
        const userOrders: EmployeeOrder[] =
          this.getEmployeesOrders.filter(o => o.userDetailsId === empDetails.id)
        const userValidations: EmployeeOrderValidation[] =
          this.getEmployeesOrdersValidations.filter(v => v.userDetailsId === empDetails.id)

        const extendedOrders: EmployeeOrderExtended[] =
          userOrders
          .map((o: EmployeeOrder) => {
            const validation: EmployeeOrderValidation | undefined =
              userValidations.find(v => v.orderId === o.id)
            const supplier: Supplier | undefined =
              useCompAdmSubscriptionStore().getSuppliers.find(s => s.id === validation?.supplierId)
            const details: OrganizationDetails | undefined =
              useCompAdmSubscriptionStore().getSuppliersDetails.find(d => d.id === supplier?.organizationDetailsId)

            return {
              order: o || new EmployeeOrder('', '', '', 0, 0, 0, 0, EmployeeOrderStatus.PendingAdminConfirmation, [], ''),
              validation: validation || new EmployeeOrderValidation(false, [], '', '', ''),
              supplierDetails: details || new OrganizationDetails(null, '', '', '', '', null),
              supplier: supplier || new Supplier(null, null, '', '', null, null),
              options: this.getSuppliersOptions
              .flatMap(so => so.options
              .filter(op => o.optionIds
              .some(id => op.id === id)))
            }
          })
          .filter(o => o.supplier && o.validation && o.supplierDetails)
          .filter(o => o.order?.id !== '' && o.validation?.orderId !== ''
            && o.supplierDetails.id && o.supplier.id)

        return {
          employeeDetails: empDetails,
          ordersExtended: extendedOrders
        }
      })
      .filter(eoi => eoi.ordersExtended.length > 0)
    },
    getEmployeesOrdersInfosForSelectedSupplier(): EmployeeOrdersInfo[] {
      return this.getEmployeesOrdersInfos
      .map(eoi => {
        const ordersForSelectedSupplier: EmployeeOrderExtended[] = eoi.ordersExtended
        .filter(o => o.supplierDetails.id === this.getSelectedSupplierDetails?.id)
        return {
          employeeDetails: eoi.employeeDetails,
          ordersExtended: ordersForSelectedSupplier
        }
      })
      .filter(eoi => eoi.ordersExtended.length > 0)
    },
    getEmployeesOrdersRecords(): EmployeesOrdersTableRecord[] {
      return this.getEmployeesOrdersInfosForSelectedSupplier
      .flatMap(i => {
        return i.ordersExtended
        .map(o => {
          return {
            orderId: o.order.id,
            supplierId: o.supplier.id ? o.supplier.id : '',
            email: i.employeeDetails.email,
            supplierName: o.supplierDetails.name,
            supplierDefaultPrice: o.order.supplierDefaultPrice,
            supplierDiscount: o.order.supplierDiscount,
            companyDiscount: o.order.companyDiscount,
            finalPrice: o.order.finalPrice,
            options: o.order.optionIds.length,
            isValid: o.validation.valid,
            status: o.order.status
          }
        })
      })
    },
    getPendingAdminConfirmationEmployeesOrdersInfos(): EmployeeOrdersInfo[] {
      return this.getEmployeesOrdersInfosForSelectedSupplier
      .filter(i => i.ordersExtended.length > 0)
      .map(i => {
        const pendingAdminConfirmationExtendedOrders: EmployeeOrderExtended[] = i.ordersExtended
        .filter(o => o.order.status === EmployeeOrderStatus.PendingAdminConfirmation)

        return {
          employeeDetails: i.employeeDetails,
          ordersExtended: pendingAdminConfirmationExtendedOrders
        }
      })
    },
    // async canOrderBeSent(): Promise<boolean> {
    //   this.companyOrderInvalidReasons = []
    //   let result: boolean = true
    //   const minimumOrdersPerCompanyRequest: number | undefined =
    //     this.getSelectedSupplierPreferences?.minimumOrdersPerCompanyRequest;
    //   const workDayStart: Date | undefined = this.getSelectedSupplierPreferences?.workDayStart
    //   const workDayEnd: Date | undefined = this.getSelectedSupplierPreferences?.workDayEnd
    //
    //   if (!minimumOrdersPerCompanyRequest || !workDayStart || !workDayEnd) {
    //     this.companyOrderInvalidReasons = ["Supplier profile not found. Try again later"]
    //     result = false
    //   } else {
    //     if (this.getEmployeesOrdersRecords.length > minimumOrdersPerCompanyRequest) {
    //       this.companyOrderInvalidReasons = [`There should be at least ${minimumOrdersPerCompanyRequest} in the order but only ${this.getEmployeesOrdersRecords.length} present`]
    //       result = false
    //     }
    //   }
    //
    //   if (this.getPendingAdminConfirmationEmployeesOrdersInfos
    //     .filter(i => {
    //       return i.ordersExtended
    //         .filter(o => o.validation.valid)
    //         .length === i.ordersExtended.length
    //     }).length === this.getPendingAdminConfirmationEmployeesOrdersInfos.length) {
    //     this.companyOrderInvalidReasons = ["There are invalid employees orders"]
    //     result = false
    //   }
    //
    //   this.getSelectedTime
    //
    //
    //   return result
    // }
  },
  actions: {
    async setSelectedDate(date: string) {
      this.selectedDate = date
      await this.requestUpdateOrdersForSelectedDateTime()
    },
    async setSelectedTime(time: string) {
      this.selectedTime = time
      await this.requestUpdateOrdersForSelectedDateTime()
    },
    async setSelectedSupplierDetails(details: OrganizationDetails) {
      this.selectedSupplierDetails = details
      await this.requestUpdateOrdersForSelectedDateTime()
    },
    async pushOrder(order: EmployeeOrder) {
      const response: AxiosResponse<EmployeeOrder> =
        await employeeOrderService.compAdmCreateEmployeeOrder(order)
      this.getEmployeesOrders.push(response.data)

      const validationResponse: AxiosResponse<EmployeeOrderValidation> =
        await this.requestValidateOrderForSelectedDate(order)
      this.employeesOrdersValidations.push(validationResponse.data)
    },
    async deleteOrder(record: EmployeesOrdersTableRecord) {
      // todo
    },
    async sendOrder() {
      if (this.getCompanyOrderValidation.valid) {
        // todo
      }
    },
    getOrderValidationByOrderId(orderId: string) {
      return this.getEmployeesOrdersValidations.find(o => o.orderId === orderId)
    },
    getOptionsOfOrder(record: EmployeesOrdersTableRecord) {
      const supplierOptions: SupplierOptions | undefined = this.getSuppliersOptions
      .find(s => s.supplier.id === record.supplierId)
      const order: EmployeeOrder | undefined = this.getEmployeesOrders
      .find(o => record.orderId === o.id)
      return supplierOptions?.options
      .filter(o => order?.optionIds.some(optionId => optionId === o.id)) || []
    },
    async requestUpdateOrdersForSelectedDateTime() {
      await this.requestOrdersForSelectedDate()
      await this.requestValidateOrdersForSelectedDateTime()

      for (const orderExtended of
        this.getEmployeesOrdersInfos
        .flatMap(eoi => eoi.ordersExtended)
        .map(o => o.supplier)) {
        if (orderExtended.id) {
          await this.requestSupplierOptionsRefreshIfEmpty(orderExtended.id)
        }
      }

      if (!this.getSelectedSupplierDetails || !this.getSuppliersDetails
      .find(d => d.id === this.getSelectedSupplier?.id)) {

        if (this.getSuppliersDetails.length > 0) {
          this.selectedSupplierDetails = this.getSuppliersDetails[0]
        }
      }

      if (this.getSelectedSupplierDetails) {
        const response: AxiosResponse<CompanyOrderValidation> =
          await companyOrderService.requestOrderValidation(new CompanyOrder(
            '',
            this.getEmployeesOrdersRecords.map(r => r.orderId),
            this.getSelectedDateTime.toISOString(),
            CompanyOrderStatus.PendingSupplierConfirmation
          ))

        this.companyOrderValidation = response.data
      }
    },
    async requestOrdersForSelectedDate() {
      const response: AxiosResponse<EmployeeOrder[]> =
        await employeeOrderService.compAdmRequestForDate(this.selectedDate)
      this.employeesOrders = response.data
    },
    async requestValidateOrdersForSelectedDateTime() {
      const response: AxiosResponse<EmployeeOrderValidation[]> =
        await employeeOrderService.compAdmRequestToValidateMultiple(this.getEmployeesOrders
        .map(o => o.id), this.getSelectedDateTime)
      this.employeesOrdersValidations = response.data
    },
    async requestSupplierOptionsRefreshIfEmpty(supplierId: string) {
      const supplierOption: SupplierOptions | undefined = this.getSuppliersOptions
      .find(s => s.supplier.id === supplierId)
      if (!supplierOption) {
        const response: AxiosResponse<Option[]> = await optionService.requestBySupplierId(supplierId)
        const supplier = useCompAdmSubscriptionStore().suppliers
        .find(s => s.id === supplierId)
        if (supplier) {
          this.getSuppliersOptions.push(
            {
              supplier: supplier,
              options: response.data
            })
        }
      }
    },
    async requestValidateOrderForSelectedDateTime(order: EmployeeOrder): Promise<AxiosResponse<EmployeeOrderValidation>> {
      return await employeeOrderService.compAdmRequestToValidateSingle(order, this.getSelectedDateTime)
    },
    async requestFreshDataIfEmpty() {
      if (this.getEmployeesOrders.length === 0) {
        await useCompAdmUserStore().requestFreshEmployeesData()
        await useCompAdmSubscriptionStore().requestDataIfEmpty()
        await useCompAdmCompPrefStore().requestDataIfEmpty()
        this.selectedTime = Utils.dateToTimeAsStringWithoutSeconds(useCompAdmCompPrefStore().getDeliveryTime || new Date())
      }
    },
  }
})

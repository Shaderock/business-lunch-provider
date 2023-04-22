import {defineStore} from "pinia";
import {SupplierPreferences} from "@/models/SupplierPreferences";
import supplierPreferencesService from "@/services/SupplierPreferencesService";
import {AxiosResponse} from "axios";
import moment, {Duration} from "moment";
import {Supplier} from "@/models/Supplier";
import supplierService from "@/services/SupplierService";
import {OrderType} from "@/models/OrderType";

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
      if (this.preferences.deliveryPeriodStartTime !== null)
        return moment(this.preferences?.deliveryPeriodStartTime, "HH:mm:ss").toDate()
      else
        return null
    },
    getEndTime(): Date | null {
      if (this.preferences.deliveryPeriodEndTime !== null)
        return moment(this.preferences.deliveryPeriodEndTime, "HH:mm:ss").toDate()
      else
        return null
    },
    arePreferencesCompleted(): boolean {
      if (!this.preferences) return false
      return !(!this.preferences.requestOffset ||
        !this.preferences.deliveryPeriodStartTime ||
        !this.preferences.deliveryPeriodEndTime ||
        !this.preferences.minimumOrdersPerCompanyRequest ||
        !this.preferences.minimumCategoriesForEmployeeOrder ||
        !this.preferences.orderType ||
        !this.preferences.deliveryPeriodEndTime ||
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

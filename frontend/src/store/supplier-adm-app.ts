import {defineStore} from "pinia";
import {SupplierPreferences} from "@/models/SupplierPreferences";
import supplierPreferencesService from "@/services/SupplierPreferencesService";
import {AxiosResponse} from "axios";
import moment, {Duration} from "moment";

export const useSupAdmSupPrefStore = defineStore('supplierAdminSupplierPreferences', {
  state: () => ({
    supplierPreferences: null as SupplierPreferences | null
  }),
  getters: {
    getPreferences(): SupplierPreferences | null {
      return this.supplierPreferences
    },
    getRequestOffset(): Duration | null {
      return moment.duration(this.supplierPreferences?.requestOffset)
    },
    getStartTime(): Date | null {
      if (this.supplierPreferences?.deliveryPeriodStartTime !== null)
        return new Date(`1970-01-01T${this.supplierPreferences?.deliveryPeriodStartTime}Z`);
      else
        return null
    },
    getEndTime(): Date | null {
      if (this.supplierPreferences?.deliveryPeriodEndTime !== null)
        return new Date(`1970-01-01T${this.supplierPreferences?.deliveryPeriodEndTime}Z`);
      else
        return null
    },
    hasPreferences(): boolean {
      return this.supplierPreferences !== null;
    },
  },
  actions: {
    clearPreferences() {
      this.supplierPreferences = null
    },
    async requestFreshPreferencesData() {
      const response: AxiosResponse<SupplierPreferences> = await supplierPreferencesService.getSupplierPreferences()
      this.supplierPreferences = response.data
    }
  }
})

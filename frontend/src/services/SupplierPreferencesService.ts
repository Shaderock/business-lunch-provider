import axios, {AxiosResponse} from "axios";
import {ApiConstants} from "@/services/ApiConstants";
import {SupplierPreferences} from "@/models/SupplierPreferences";
import {Utils} from "@/models/Utils";
import {PublicSupplierPreferences} from "@/models/PublicSupplierPreferences";

export class SupplierPreferencesService {

  public getSupplierPreferences(): Promise<AxiosResponse<SupplierPreferences>> {
    return axios.get(ApiConstants.SUPPLIER_ADM_PREFERENCES + "/my");
  }

  public anonymousRequestForAllPreferences(): Promise<AxiosResponse<SupplierPreferences[]>> {
    return axios.get(`${ApiConstants.ANONYM_SUPPLIER_PREFERENCES}/all`);
  }

  update(preferences: SupplierPreferences): Promise<AxiosResponse<SupplierPreferences>> {
    const workDayStartString: string = Utils.dateToTimeAsString(preferences.workDayStart)
    const workDayEndString: string = Utils.dateToTimeAsString(preferences.workDayEnd)

    const serializedPreferences = {
      ...preferences,
      workDayStart: workDayStartString,
      workDayEnd: workDayEndString
    };

    return axios.put(ApiConstants.SUPPLIER_ADM_PREFERENCES, serializedPreferences);
  }

  async requestSubscriptionSuppliersPreferences(): Promise<AxiosResponse<PublicSupplierPreferences[]>> {
    return axios.get(`${ApiConstants.COMPANY_ADM_SUPPLIER_PREFERENCES}/subscription/all`)
  }

  async requestPreferencesForSupplier(supplierName: string): Promise<AxiosResponse<PublicSupplierPreferences>> {
    const params: URLSearchParams = new URLSearchParams([['supplierName', supplierName]])
    return axios.get(`${ApiConstants.ANONYM_SUPPLIER_PREFERENCES}`, {params});
  }
}

const supplierPreferencesService: SupplierPreferencesService = new SupplierPreferencesService()
export default supplierPreferencesService

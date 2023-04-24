import axios, {AxiosResponse} from "axios";
import {ApiConstants} from "@/services/ApiConstants";
import {SupplierPreferences} from "@/models/SupplierPreferences";
import {Utils} from "@/models/Utils";

export class SupplierPreferencesService {

  public getSupplierPreferences(): Promise<AxiosResponse<SupplierPreferences>> {
    return axios.get(ApiConstants.SUPPLIER_ADM_PREFERENCES + "/my");
  }

  public anonymousRequestForPreferences(): Promise<AxiosResponse<SupplierPreferences[]>> {
    return axios.get(`${ApiConstants.ANONYM_SUPPLIER_PREFERENCES}/all`);
  }

  update(preferences: SupplierPreferences): Promise<AxiosResponse<SupplierPreferences>> {
    const deliveryPeriodStartTimeString: string = Utils.dateToTimeAsString(preferences.deliveryPeriodStartTime)
    const deliveryPeriodEndTimeString: string = Utils.dateToTimeAsString(preferences.deliveryPeriodEndTime)

    const serializedPreferences = {
      ...preferences,
      deliveryPeriodStartTime: deliveryPeriodStartTimeString,
      deliveryPeriodEndTime: deliveryPeriodEndTimeString
    };

    return axios.put(ApiConstants.SUPPLIER_ADM_PREFERENCES, serializedPreferences);
  }
}

const supplierPreferencesService: SupplierPreferencesService = new SupplierPreferencesService()
export default supplierPreferencesService

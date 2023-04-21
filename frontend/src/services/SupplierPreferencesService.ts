import axios, {AxiosResponse} from "axios";
import {ApiConstants} from "@/services/ApiConstants";
import {SupplierPreferences} from "@/models/SupplierPreferences";

export class SupplierPreferencesService {

  public getSupplierPreferences(): Promise<AxiosResponse<SupplierPreferences>> {
    return axios.get(ApiConstants.SUPPLIER_ADM_PREFERENCES + "/my");
  }

  update(preferences: SupplierPreferences): Promise<AxiosResponse<SupplierPreferences>> {

    // return axios.put(ApiConstants.SUPPLIER_ADM_PREFERENCES, preferences)

    const deliveryPeriodStartTimeString: string = preferences.deliveryPeriodStartTime.toISOString().substring(11, 19)
    const deliveryPeriodEndTimeString: string = preferences.deliveryPeriodEndTime.toISOString().substring(11, 19)

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

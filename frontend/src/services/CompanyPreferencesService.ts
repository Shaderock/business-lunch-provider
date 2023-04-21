import axios, {AxiosResponse} from "axios";
import {ApiConstants} from "@/services/ApiConstants";
import {Utils} from "@/models/Utils";
import {CompanyPreferences} from "@/models/CompanyPreferences";

export class CompanyPreferencesService {

  public getCompanyPreferences(): Promise<AxiosResponse<CompanyPreferences>> {
    return axios.get(ApiConstants.COMPANY_ADM_COMPANY_PREFERENCES);
  }

  update(preferences: CompanyPreferences): Promise<AxiosResponse<CompanyPreferences>> {
    const deliveryAtString: string = Utils.dateToTimeAsString(preferences.deliverAt)
    console.log(deliveryAtString)
    console.log(Utils.dateToTimeAsString(preferences.deliverAt))

    const serializedPreferences = {
      ...preferences,
      deliverAt: deliveryAtString,
    };

    return axios.put(ApiConstants.COMPANY_ADM_COMPANY_PREFERENCES, serializedPreferences);
  }
}

const companyPreferencesService: CompanyPreferencesService = new CompanyPreferencesService()
export default companyPreferencesService

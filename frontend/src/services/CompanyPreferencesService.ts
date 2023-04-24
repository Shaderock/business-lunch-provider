import axios, {AxiosResponse} from "axios";
import {ApiConstants} from "@/services/ApiConstants";
import {Utils} from "@/models/Utils";
import {CompanyPreferences} from "@/models/CompanyPreferences";
import {PublicCompanyPreferences} from "@/models/PublicCompanyPreferences";

export class CompanyPreferencesService {

  public getCompanyPreferences(): Promise<AxiosResponse<CompanyPreferences>> {
    return axios.get(ApiConstants.COMPANY_ADM_COMPANY_PREFERENCES);
  }

  update(preferences: CompanyPreferences): Promise<AxiosResponse<CompanyPreferences>> {
    const deliveryAtString: string = Utils.dateToTimeAsString(preferences.deliverAt)

    const serializedPreferences = {
      ...preferences,
      deliverAt: deliveryAtString,
    };

    return axios.put(ApiConstants.COMPANY_ADM_COMPANY_PREFERENCES, serializedPreferences);
  }

  async requestSubscribersCompaniesPreferences(): Promise<AxiosResponse<PublicCompanyPreferences[]>> {
    return axios.get(`${ApiConstants.SUPPLIER_ADM_COMPANY_PREFERENCES}/subscriber/all`)
  }
}

const companyPreferencesService: CompanyPreferencesService = new CompanyPreferencesService()
export default companyPreferencesService

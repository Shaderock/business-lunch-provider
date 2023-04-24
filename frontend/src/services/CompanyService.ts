import axios from "axios";
import {ApiConstants} from "@/services/ApiConstants";

export class CompanyService {

  public register(name: string): Promise<any> {
    return axios.post(`${ApiConstants.COMPANY}/register`, {
      name: name,
    });
  }

  public getUserCompany(): Promise<any> {
    return axios.get(ApiConstants.COMPANY + '/my')
  }

  async getAllCompanies(): Promise<any> {
    return axios.get(ApiConstants.SYS_ADM_COMPANY)
  }

  async requestSupplierSubscribers(): Promise<any> {
    return axios.get(`${ApiConstants.SUPPLIER_ADM_COMPANY}/subscriber/all`)
  }
}

const companyService: CompanyService = new CompanyService()
export default companyService

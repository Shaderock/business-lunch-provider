import axios from "axios";
import {ApiConstants} from "@/services/ApiConstants";

export class CompanyService {
  public getUserCompany(): Promise<any> {
    return axios.get(ApiConstants.COMPANY + '/my')
  }

  async getAllCompanies(): Promise<any> {
    return axios.get(ApiConstants.SYS_ADM_COMPANY)
  }
}

const companyService: CompanyService = new CompanyService()
export default companyService

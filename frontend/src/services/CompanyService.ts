import {OrganizationService} from "./OrganizationService";
import axios from "axios";
import {ApiConstants} from "@/services/ApiConstants";

export class CompanyService extends OrganizationService {
  public register(email: string, name: string, phone: string): Promise<any> {
    return axios.post(ApiConstants.COMPANY + "/register", {
      email: email,
      name: name,
      phone: phone
    });
  }

  public getUserCompany(): Promise<any> {
    return axios.get(ApiConstants.COMPANY + '/my')
  }

  async getAllCompanies(): Promise<any> {
    return axios.get(ApiConstants.SYS_ADM_COMPANY)
  }
}

const companyService: CompanyService = new CompanyService()
export default companyService

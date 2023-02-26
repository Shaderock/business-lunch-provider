import {AppSettings} from "./AppSettings";
import {OrganizationService} from "./OrganizationService";
import axios from "axios";

export class CompanyService extends OrganizationService {
  private companyUrl = AppSettings.API_URL + '/company'
  private sysAdmCompaniesUrl = AppSettings.API_URL + '/sysadm/company'
  private registerUrl = this.companyUrl + "/register"
  private userCompanyUrl = this.companyUrl + '/my'

  public register(email: string, name: string, phone: string): Promise<any> {
    return axios.post(this.registerUrl, {
      email: email,
      name: name,
      phone: phone
    });
  }

  public getUserCompany(): Promise<any> {
    return axios.get(this.userCompanyUrl)
  }

  async getAllCompanies(): Promise<any> {
    return axios.get(this.sysAdmCompaniesUrl)
  }
}

const companyService: CompanyService = new CompanyService()
export default companyService

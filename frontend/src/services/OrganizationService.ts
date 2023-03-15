import {AppSettings} from "./AppSettings";
import axios from "axios";

export class OrganizationService {
  protected organizationUrl = AppSettings.API_URL + '/organization'
  protected sysAdmOrganizationUrl = AppSettings.API_URL + '/sys-adm/organization/details'
  protected organizationNameVerifyUrl = this.organizationUrl + '/verify-name'
  protected organizationEmailVerifyUrl = this.organizationUrl + '/verify-email'

  public validateOrganizationName(name: string): Promise<any> {
    const params = new URLSearchParams([['name', name]])
    return axios.get(this.organizationNameVerifyUrl, {params});
  }

  public validateOrganizationEmail(email: string): Promise<any> {
    const params = new URLSearchParams([['email', email]])
    return axios.get(this.organizationEmailVerifyUrl, {params});
  }

  public getAllOrganizations(): Promise<any> {
    return axios.get(this.sysAdmOrganizationUrl);
  }
}

const organizationService: OrganizationService = new OrganizationService()
export default organizationService

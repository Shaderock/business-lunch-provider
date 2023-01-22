import {AppSettings} from "./AppSettings";
import axios, {AxiosResponse} from "axios";

export class OrganizationService {
  protected organizationUrl = AppSettings.API_URL + '/organization'
  protected organizationNameVerifyUrl = this.organizationUrl + '/verify-name'
  protected organizationEmailVerifyUrl = this.organizationUrl + '/verify-email'

  public validateOrganizationName(name: string): Promise<AxiosResponse<any, any>> {
    const params = new URLSearchParams([['name', name]])
    return axios.get(this.organizationNameVerifyUrl, {params});
  }

  public validateOrganizationEmail(email: string): Promise<AxiosResponse<any, any>> {
    const params = new URLSearchParams([['email', email]])
    return axios.get(this.organizationEmailVerifyUrl, {params});
  }
}

const organizationService: OrganizationService = new OrganizationService()
export default organizationService

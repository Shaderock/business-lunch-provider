import axios from "axios";
import {ApiConstants} from "@/services/ApiConstants";

export class OrganizationService {
  public validateOrganizationName(name: string): Promise<any> {
    const params: URLSearchParams = new URLSearchParams([['name', name]])
    return axios.get(ApiConstants.ORGANIZATION + '/verify-name', {params});
  }

  public validateOrganizationEmail(email: string): Promise<any> {
    const params: URLSearchParams = new URLSearchParams([['email', email]])
    return axios.get(ApiConstants.ORGANIZATION + '/verify-email', {params});
  }

  public getAllOrganizations(): Promise<any> {
    return axios.get(ApiConstants.SYS_ADM_ORGANIZATION);
  }
}

const organizationService: OrganizationService = new OrganizationService()
export default organizationService

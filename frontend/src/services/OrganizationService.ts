import axios, {AxiosResponse} from "axios";
import {ApiConstants} from "@/services/ApiConstants";
import {OrganizationDetails} from "@/models/OrganizationDetails";

export class OrganizationService {

  public register(name: string, isSupplier: boolean): Promise<any> {
    let url: string = ApiConstants.COMPANY;
    if (isSupplier)
      url = ApiConstants.SUPPLIER;

    return axios.post(url + "/register", {
      name: name,
    });
  }

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

  public getUserOrganization(): Promise<AxiosResponse<OrganizationDetails>> {
    return axios.get(ApiConstants.ORGANIZATION + "/my");
  }

  update(organizationDetails: OrganizationDetails): Promise<AxiosResponse<OrganizationDetails>> {
    return axios.put(ApiConstants.ORGANIZATION_ADM_ORGANIZATION, organizationDetails)
  }
}

const organizationService: OrganizationService = new OrganizationService()
export default organizationService

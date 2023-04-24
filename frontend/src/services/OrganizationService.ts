import axios, {AxiosResponse} from "axios";
import {ApiConstants} from "@/services/ApiConstants";
import {OrganizationDetails} from "@/models/OrganizationDetails";
import {PublicOrganizationDetails} from "@/models/PublicOrganizationDetails";
import {Utils} from "@/models/Utils";

export class OrganizationService {
  public validateOrganizationName(name: string): Promise<any> {
    const params: URLSearchParams = new URLSearchParams([['name', name]])
    return axios.get(ApiConstants.ORGANIZATION + '/verify-name', {params});
  }

  public validateOrganizationEmail(email: string): Promise<any> {
    const params: URLSearchParams = new URLSearchParams([['email', email]])
    return axios.get(ApiConstants.ORGANIZATION + '/verify-email', {params});
  }

  public getAllOrganizations(): Promise<AxiosResponse<OrganizationDetails>> {
    return axios.get(ApiConstants.SYS_ADM_ORGANIZATION);
  }

  public getUserOrganization(): Promise<AxiosResponse<OrganizationDetails>> {
    return axios.get(ApiConstants.ORGANIZATION + "/my");
  }

  update(organizationDetails: OrganizationDetails): Promise<AxiosResponse<OrganizationDetails>> {
    return axios.put(ApiConstants.ORGANIZATION_ADM_ORGANIZATION, organizationDetails)
  }

  async getAllUserInvitingOrganizations(): Promise<AxiosResponse<PublicOrganizationDetails[]>> {
    return axios.get(`${ApiConstants.ORGANIZATION}/invitation/all`);
  }

  async anonymousRequestForDetails(): Promise<AxiosResponse<PublicOrganizationDetails[]>> {
    return axios.get(`${ApiConstants.ANONYM_ORGANIZATION}/supplier/all`);
  }

  async requestOrganizationLogo(): Promise<string> {
    const params: URLSearchParams = new URLSearchParams([['isThumbnail', 'false']])
    const response = await axios.get(`${ApiConstants.ORGANIZATION}/my/logo`, {
      params,
      responseType: 'arraybuffer'
    });
    return Utils.byteArrayToBase64String(response.data)
  }

  async updateOrganizationLogo(file: File): Promise<void> {
    const formData: FormData = new FormData();
    formData.append('logo', file)
    console.log(formData)
    console.log(formData.get('logo'))
    return axios.put(`${ApiConstants.ORGANIZATION_ADM_ORGANIZATION}/logo`, formData);
  }

  async requestOrganizationLogoThumbnail(): Promise<string> {
    const params: URLSearchParams = new URLSearchParams([['isThumbnail', 'true']])
    const response = await axios.get(`${ApiConstants.ORGANIZATION}/my/logo`, {
      params,
      responseType: 'arraybuffer'
    });
    return Utils.byteArrayToBase64String(response.data)
  }
}

const organizationService: OrganizationService = new OrganizationService()
export default organizationService

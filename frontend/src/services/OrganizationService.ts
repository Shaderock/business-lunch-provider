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

  async anonymousRequestForAllDetails(): Promise<AxiosResponse<PublicOrganizationDetails[]>> {
    return axios.get(`${ApiConstants.ANONYM_ORGANIZATION}/supplier/all`);
  }

  async requestInvitingCompanyLogo(companyId: string, isThumbnail: boolean): Promise<string> {
    const params: URLSearchParams = new URLSearchParams([['isThumbnail', isThumbnail + ''], ['companyId', companyId]])
    const response = await axios.get(`${ApiConstants.ORGANIZATION}/invitation/logo`, {
      params,
      responseType: 'arraybuffer'
    });
    return Utils.byteArrayToBase64String(response.data)
  }

  async requestUserOrganizationLogo(isThumbnail: boolean): Promise<string> {
    const params: URLSearchParams = new URLSearchParams([['isThumbnail', isThumbnail + '']])
    const response = await axios.get(`${ApiConstants.ORGANIZATION}/my/logo`, {
      params,
      responseType: 'arraybuffer'
    });
    return Utils.byteArrayToBase64String(response.data)
  }

  async updateOrganizationLogo(file: File): Promise<void> {
    const formData: FormData = new FormData();
    formData.append('logo', file)
    return axios.put(`${ApiConstants.ORGANIZATION_ADM_ORGANIZATION}/logo`, formData);
  }

  async requestSubscriptionSuppliersDetails(): Promise<AxiosResponse<OrganizationDetails[]>> {
    return axios.get(`${ApiConstants.EMPLOYEE_ORGANIZATION}/subscription/all`)
  }

  async requestSubscribersCompaniesDetails() {
    return axios.get(`${ApiConstants.SUPPLIER_ADM_ORGANIZATION}/subscriber/all`)
  }

  async requestDetailsForSupplier(supplierName: string): Promise<AxiosResponse<PublicOrganizationDetails>> {
    const params: URLSearchParams = new URLSearchParams([['supplierName', supplierName]])
    return axios.get(`${ApiConstants.ANONYM_ORGANIZATION}/supplier`, {params});
  }
}

const organizationService: OrganizationService = new OrganizationService()
export default organizationService

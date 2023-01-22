import {AppSettings} from "./AppSettings";
import {OrganizationService} from "./OrganizationService";
import axios, {AxiosResponse} from "axios";

export class SupplierService extends OrganizationService {
  private supplierUrl = AppSettings.API_URL + '/supplier'
  private registerUrl = this.supplierUrl + "/registerCompany"
  private userSupplierUrl = this.supplierUrl + '/my'

  public register(email: string, name: string, description: string,
                  phone: string, websiteUrl: string, menuUrl: string): Promise<any> {
    return axios.post(this.registerUrl, {
      email: email,
      name: name,
      phone: phone
    });
  }

  public getUserSupplier(): Promise<AxiosResponse<any, any>> {
    return axios.get(this.userSupplierUrl)
  }
}

const supplierService: SupplierService = new SupplierService()
export default supplierService

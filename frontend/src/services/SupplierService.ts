import {OrganizationService} from "./OrganizationService";
import axios from "axios";
import {ApiConstants} from "@/services/ApiConstants";

export class SupplierService extends OrganizationService {

  public register(name: string): Promise<any> {
    return axios.post(ApiConstants.SUPPLIER + "/register", {
      name: name,
    });
  }

  public getSupplierOfCurrentUser(): Promise<any> {
    return axios.get(ApiConstants.SUPPLIER + '/my')
  }

  public getAllSuppliers(): Promise<any> {
    return axios.get(ApiConstants.SYS_ADM_SUPPLIER)
  }
}

const supplierService: SupplierService = new SupplierService()
export default supplierService

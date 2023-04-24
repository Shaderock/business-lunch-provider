import axios from "axios";
import {ApiConstants} from "@/services/ApiConstants";
import {Supplier} from "@/models/Supplier";

export class SupplierService {

  public register(name: string): Promise<any> {
    return axios.post(`${ApiConstants.SUPPLIER}/register`, {
      name: name,
    });
  }

  public updateSupplier(supplier: Supplier) {
    return axios.put(ApiConstants.SUPPLIER_ADM_SUPPLIER, supplier)
  }

  public getUserSupplier(): Promise<any> {
    return axios.get(ApiConstants.SUPPLIER_ADM_SUPPLIER + '/my')
  }

  public anonymousRequestForSuppliers(): Promise<any> {
    return axios.get(`${ApiConstants.ANONYM_SUPPLIER}/all`)
  }

  public getAllSuppliers(): Promise<any> {
    return axios.get(ApiConstants.SYS_ADM_SUPPLIER)
  }
}

const supplierService: SupplierService = new SupplierService()
export default supplierService

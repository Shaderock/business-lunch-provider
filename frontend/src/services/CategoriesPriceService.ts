import axios, {AxiosResponse} from "axios";
import {ApiConstants} from "@/services/ApiConstants";
import {CategoriesPrice} from "@/models/CategoriesPrice";

export class CategoriesPriceService {
  async requestAllAsSupplierAdmin(): Promise<AxiosResponse<CategoriesPrice[]>> {
    return axios.get(`${ApiConstants.SUPPLIER_ADM_CATEGORIES_PRICE}/all`)
  }

  async requestBySupplierId(supplierId: string): Promise<AxiosResponse<CategoriesPrice[]>> {
    const params: URLSearchParams = new URLSearchParams([['supplierId', supplierId]])
    return axios.get(`${ApiConstants.ANONYM_CATEGORIES_PRICE}`, {params});
  }

  async createPriceForCategories(pfc: CategoriesPrice): Promise<AxiosResponse<CategoriesPrice>> {
    return axios.post(`${ApiConstants.SUPPLIER_ADM_CATEGORIES_PRICE}`, pfc);
  }

  async updatePriceForCategories(pfc: CategoriesPrice): Promise<AxiosResponse<CategoriesPrice>> {
    return axios.put(`${ApiConstants.SUPPLIER_ADM_CATEGORIES_PRICE}`, pfc);
  }

  async removePriceForCategories(): Promise<void> {
    return axios.delete(`${ApiConstants.SUPPLIER_ADM_CATEGORIES_PRICE}`);
  }
}

const categoriesPriceService: CategoriesPriceService = new CategoriesPriceService()
export default categoriesPriceService

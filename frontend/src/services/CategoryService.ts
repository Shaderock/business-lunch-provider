import axios, {AxiosResponse} from "axios";
import {ApiConstants} from "@/services/ApiConstants";
import {Category} from "@/models/Category";

export class CategoryService {
  async requestSupplierCategories(): Promise<AxiosResponse<Category[]>> {
    return axios.get(`${ApiConstants.SUPPLIER_ADM_CATEGORY}/all`);
  }

  async createCategory(category: Category): Promise<AxiosResponse<Category>> {
    return axios.post(ApiConstants.SUPPLIER_ADM_CATEGORY, category);
  }

  async deleteCategory(categoryId: string): Promise<AxiosResponse<Category>> {
    const params: URLSearchParams = new URLSearchParams([['categoryId', categoryId]])
    return axios.delete(ApiConstants.SUPPLIER_ADM_CATEGORY, {params})
  }

  async updateCategory(category: Category): Promise<AxiosResponse<Category>> {
    return axios.put(ApiConstants.SUPPLIER_ADM_CATEGORY, category);
  }
}

const categoryService: CategoryService = new CategoryService()
export default categoryService

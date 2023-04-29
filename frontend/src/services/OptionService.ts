import axios, {AxiosResponse} from "axios";
import {ApiConstants} from "@/services/ApiConstants";
import {Option} from "@/models/Option";
import {Utils} from "@/models/Utils";

export class OptionService {
  async requestSupplierOptions(): Promise<AxiosResponse<Option[]>> {
    return axios.get(`${ApiConstants.SUPPLIER_ADM_OPTION}/all`);
  }

  async createOption(option: Option, categoryId: string): Promise<AxiosResponse<Option>> {
    const params: URLSearchParams = new URLSearchParams([['categoryId', categoryId]])
    return axios.post(ApiConstants.SUPPLIER_ADM_OPTION, option, {params});
  }

  async deleteOption(optionId: string): Promise<AxiosResponse<Option>> {
    const params: URLSearchParams = new URLSearchParams([['optionId', optionId]])
    return axios.delete(ApiConstants.SUPPLIER_ADM_OPTION, {params})
  }

  async updateOption(option: Option, categoryId: string): Promise<AxiosResponse<Option>> {
    const params: URLSearchParams = new URLSearchParams([['categoryId', categoryId]])
    return axios.put(ApiConstants.SUPPLIER_ADM_OPTION, option, {params});
  }

  async deletePhoto(optionId: string): Promise<AxiosResponse<Option>> {
    const params: URLSearchParams = new URLSearchParams([['optionId', optionId]])
    return axios.delete(`${ApiConstants.SUPPLIER_ADM_OPTION}/photo`, {params})
  }

  async updatePhoto(file: File, optionId: string): Promise<void> {
    const params: URLSearchParams = new URLSearchParams([['optionId', optionId]])
    const formData: FormData = new FormData();
    formData.append('photo', file)
    return axios.put(`${ApiConstants.SUPPLIER_ADM_OPTION}/photo`, formData, {params});
  }

  async requestOptionPhotoAsSupplier(optionId: string, isThumbnail: boolean): Promise<string> {
    let urlPart: string = '/thumbnail'
    if (!isThumbnail) {
      urlPart = ''
    }
    const params: URLSearchParams = new URLSearchParams([['optionId', optionId]])
    const response = await axios.get(`${ApiConstants.SUPPLIER_ADM_OPTION}/photo${urlPart}`, {
      params,
      responseType: 'arraybuffer'
    });
    return Utils.byteArrayToBase64String(response.data)
  }

  async requestOptionsForCategory(categoryId: string): Promise<AxiosResponse<Option[]>> {
    const params: URLSearchParams = new URLSearchParams([['categoryId', categoryId]])
    return axios.get(`${ApiConstants.ANONYM_OPTION}/by-category-id`, {params});
  }

  async requestByOrderForDate(date: string): Promise<AxiosResponse<Option[]>> {
    const params: URLSearchParams = new URLSearchParams([['date', date]])
    return axios.get(`${ApiConstants.EMPLOYEE_OPTION}`, {params});
  }

  async requestBySupplierId(supplierId: string): Promise<AxiosResponse<Option[]>> {
    const params: URLSearchParams = new URLSearchParams([['supplierId', supplierId]])
    return axios.get(`${ApiConstants.ANONYM_OPTION}/by-supplier-id`, {params});
  }
}

const optionService: OptionService = new OptionService()
export default optionService

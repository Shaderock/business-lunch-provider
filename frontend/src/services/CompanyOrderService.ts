import axios, {AxiosResponse} from "axios";
import {ApiConstants} from "@/services/ApiConstants";
import {CompanyOrder} from "@/models/CompanyOrder";
import {CompanyOrderValidation} from "@/models/CompanyOrderValidation";

export class CompanyOrderService {

  async requestOrderValidation(companyOrder: CompanyOrder): Promise<AxiosResponse<CompanyOrderValidation>> {
    return axios.post(`${ApiConstants.COMPANY_ADM_COMPANY_ORDER}/validate`, companyOrder)
  }

  async createOrder(companyOrder: CompanyOrder): Promise<AxiosResponse<CompanyOrder>> {
    return axios.post(`${ApiConstants.COMPANY_ADM_COMPANY_ORDER}`, companyOrder);
  }

  async requestForDate(date: string) {
    const params: URLSearchParams = new URLSearchParams([['date', date]])
    return axios.get(`${ApiConstants.COMPANY_ADM_COMPANY_ORDER}`, {params});
  }

  async deleteOrder(orderId: string) {
    const params: URLSearchParams = new URLSearchParams([['orderId', orderId]])
    return axios.delete(`${ApiConstants.COMPANY_ADM_COMPANY_ORDER}`, {params});
  }
}

const companyOrderService: CompanyOrderService = new CompanyOrderService()
export default companyOrderService

import axios, {AxiosResponse} from "axios";
import {ApiConstants} from "@/services/ApiConstants";
import {Subscription} from "@/models/Subscription";

export class SubscriptionService {
  async requestCompanySubscriptions(): Promise<AxiosResponse<Subscription[]>> {
    return axios.get(`${ApiConstants.EMPLOYEE_COMPANY_SUBSCRIPTION}/all`)
  }

  async subscribe(supplierId: string): Promise<void> {
    const params: URLSearchParams = new URLSearchParams([['supplierId', supplierId]])
    return axios.post(`${ApiConstants.COMPANY_ADM_SUBSCRIPTION}/subscribe`, null, {params})
  }

  async unsubscribe(supplierId: string): Promise<void> {
    const params: URLSearchParams = new URLSearchParams([['supplierId', supplierId]])
    return axios.delete(`${ApiConstants.COMPANY_ADM_SUBSCRIPTION}/unsubscribe`, {params})
  }

  async requestSupplierSubscribers() {
    return axios.get(`${ApiConstants.SUPPLIER_ADM_SUBSCRIPTION}/all`)
  }

  async declineSubscription(companyId: string) {
    const params: URLSearchParams = new URLSearchParams([['companyId', companyId]])
    return axios.delete(`${ApiConstants.SUPPLIER_ADM_SUBSCRIPTION}/decline`, {params})
  }

  async acceptSubscription(companyId: string) {
    const params: URLSearchParams = new URLSearchParams([['companyId', companyId]])
    return axios.post(`${ApiConstants.SUPPLIER_ADM_SUBSCRIPTION}/accept`, null, {params})
  }
}

const subscriptionService: SubscriptionService = new SubscriptionService()
export default subscriptionService

import axios, {AxiosResponse} from "axios";
import {EmployeeOrderValidation} from "@/models/EmployeeOrderValidation";
import {ApiConstants} from "@/services/ApiConstants";
import {EmployeeOrder} from "@/models/EmployeeOrder";

export class EmployeeOrderService {

  async requestOrderValidation(employeeOrder: EmployeeOrder): Promise<AxiosResponse<EmployeeOrderValidation>> {
    return axios.post(`${ApiConstants.EMPLOYEE_ORDER}/validate`, employeeOrder)
  }

  async requestOrderCalculation(employeeOrder: EmployeeOrder): Promise<AxiosResponse<EmployeeOrder>> {
    return axios.post(`${ApiConstants.EMPLOYEE_ORDER}/calculate`, employeeOrder);
  }

  async createOrder(employeeOrder: EmployeeOrder): Promise<AxiosResponse<EmployeeOrder>> {
    return axios.post(`${ApiConstants.EMPLOYEE_ORDER}`, employeeOrder);
  }

  async requestForDate(date: string) {
    const params: URLSearchParams = new URLSearchParams([['date', date]])
    return axios.get(`${ApiConstants.EMPLOYEE_ORDER}`, {params});
  }

  async deleteOrder(orderId: string) {
    const params: URLSearchParams = new URLSearchParams([['orderId', orderId]])
    return axios.delete(`${ApiConstants.EMPLOYEE_ORDER}`, {params});
  }
}

const employeeOrderService: EmployeeOrderService = new EmployeeOrderService()
export default employeeOrderService

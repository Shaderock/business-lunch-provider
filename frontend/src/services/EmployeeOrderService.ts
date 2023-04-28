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
}

const employeeOrderService: EmployeeOrderService = new EmployeeOrderService()
export default employeeOrderService

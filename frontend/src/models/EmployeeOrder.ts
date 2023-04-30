import {EmployeeOrderStatus} from "@/models/EmployeeOrderStatus";

export class EmployeeOrder {
  id: string
  userDetailsId: string
  companyOrderId: string
  supplierDefaultPrice: number
  supplierDiscount: number
  companyDiscount: number
  finalPrice: number
  status: EmployeeOrderStatus
  optionIds: string[]
  orderDate: string


  constructor(id: string, userDetailsId: string, companyOrderId: string, supplierDefaultPrice: number, supplierDiscount: number, companyDiscount: number, finalPrice: number, status: EmployeeOrderStatus, optionIds: string[], orderDate: string) {
    this.id = id;
    this.userDetailsId = userDetailsId;
    this.companyOrderId = companyOrderId;
    this.supplierDefaultPrice = supplierDefaultPrice;
    this.supplierDiscount = supplierDiscount;
    this.companyDiscount = companyDiscount;
    this.finalPrice = finalPrice;
    this.status = status;
    this.optionIds = optionIds;
    this.orderDate = orderDate;
  }
}

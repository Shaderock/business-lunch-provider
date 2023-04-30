import {CompanyOrderStatus} from "@/models/CompanyOrderStatus";

export class CompanyOrder {
  id: string
  employeesOrderIds: string[]
  deliverAt: string
  createdAt: string
  status: CompanyOrderStatus
  companyName: string

  constructor(id: string, employeesOrderIds: string[], deliverAt: string, createdAt: string, status: CompanyOrderStatus, companyName: string) {
    this.id = id;
    this.employeesOrderIds = employeesOrderIds;
    this.deliverAt = deliverAt;
    this.createdAt = createdAt;
    this.status = status;
    this.companyName = companyName;
  }
}

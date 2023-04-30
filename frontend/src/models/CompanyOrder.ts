export class CompanyOrder {
  id: string
  employeesOrderIds: string[]
  deliverAt: string
  status: string

  constructor(id: string, employeesOrderIds: string[], deliverAt: string, status: string) {
    this.id = id;
    this.employeesOrderIds = employeesOrderIds;
    this.deliverAt = deliverAt;
    this.status = status;
  }
}

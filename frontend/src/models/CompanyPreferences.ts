import {CompanyDiscountType} from "@/models/CompanyDiscountType";

export class CompanyPreferences {
  public id: string | null
  public companyId: string | null
  public companyDiscountType: CompanyDiscountType
  public discountPercentageFirstOrder: number
  public discountFixFirstOrder: number
  public maxDiscountFixFirstOrder: number
  public discountPerDay: number
  public deliverAt: Date
  public deliveryAddress: string

  constructor(id: string | null,
              companyId: string | null,
              companyDiscountType:
                CompanyDiscountType,
              discountPercentageFirstOrder: number,
              discountFixFirstOrder: number,
              maxDiscountFixFirstOrder: number,
              discountPerDay: number,
              deliverAt: Date,
              deliveryAddress: string) {
    this.id = id;
    this.companyId = companyId;
    this.companyDiscountType = companyDiscountType;
    this.discountPercentageFirstOrder = discountPercentageFirstOrder;
    this.discountFixFirstOrder = discountFixFirstOrder;
    this.maxDiscountFixFirstOrder = maxDiscountFixFirstOrder;
    this.discountPerDay = discountPerDay;
    this.deliverAt = deliverAt;
    this.deliveryAddress = deliveryAddress;
  }
}

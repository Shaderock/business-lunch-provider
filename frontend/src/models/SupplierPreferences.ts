import {Duration} from "moment";
import {OrderType} from "@/models/OrderType";
import {CategoryTag} from "@/models/CategoryTag";

export class SupplierPreferences {
  public id: string | null
  public supplierId: string | null
  public requestOffset: Duration
  public deliveryPeriodStartTime: Date
  public deliveryPeriodEndTime: Date
  public minimumOrdersPerCompanyRequest: number
  public minimumCategoriesForEmployeeOrder: number
  public orderType: OrderType
  public pricesForCategoriesIds: string[] | null
  public orderCapacityId: string | null
  public categoriesTags: CategoryTag[]

  constructor(id: string | null,
              supplierId: string | null,
              requestOffset: Duration,
              deliveryPeriodStartTime: Date,
              deliveryPeriodEndTime: Date,
              minimumOrdersPerCompanyRequest: number,
              minimumCategoriesForEmployeeOrder: number,
              orderType: OrderType,
              pricesForCategoriesIds: string[] | null,
              orderCapacityId: string | null,
              categoriesTags: CategoryTag[]) {
    this.id = id
    this.supplierId = supplierId
    this.requestOffset = requestOffset
    this.deliveryPeriodStartTime = deliveryPeriodStartTime
    this.deliveryPeriodEndTime = deliveryPeriodEndTime
    this.minimumOrdersPerCompanyRequest = minimumOrdersPerCompanyRequest
    this.minimumCategoriesForEmployeeOrder = minimumCategoriesForEmployeeOrder
    this.orderType = orderType
    this.pricesForCategoriesIds = pricesForCategoriesIds
    this.orderCapacityId = orderCapacityId
    this.categoriesTags = categoriesTags
  }
}

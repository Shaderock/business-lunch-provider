import {Duration} from "moment";
import {OrderType} from "@/models/OrderType";
import {CategoryTag} from "@/models/CategoryTag";

export class PublicSupplierPreferences {
  public id: string | null
  public requestOffset: Duration
  public workDayStart: Date
  public workDayEnd: Date
  public minimumOrdersPerCompanyRequest: number
  public minimumCategoriesForEmployeeOrder: number
  public orderType: OrderType
  public pricesForCategoriesIds: string[] | null
  public categoriesTags: CategoryTag[]

  constructor(id: string | null,
              requestOffset: Duration,
              workDayStart: Date,
              workDayEnd: Date,
              minimumOrdersPerCompanyRequest: number,
              minimumCategoriesForEmployeeOrder: number,
              orderType: OrderType,
              pricesForCategoriesIds: string[] | null,
              categoriesTags: CategoryTag[]) {
    this.id = id
    this.requestOffset = requestOffset
    this.workDayStart = workDayStart
    this.workDayEnd = workDayEnd
    this.minimumOrdersPerCompanyRequest = minimumOrdersPerCompanyRequest
    this.minimumCategoriesForEmployeeOrder = minimumCategoriesForEmployeeOrder
    this.orderType = orderType
    this.pricesForCategoriesIds = pricesForCategoriesIds
    this.categoriesTags = categoriesTags
  }
}

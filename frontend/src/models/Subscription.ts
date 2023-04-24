import {SubscriptionStatus} from "@/models/SubscriptionStatus";

export class Subscription {
  public id: string
  public companyId: string
  public supplierId: string
  public subscriptionStatus: SubscriptionStatus
  public createdAt: Date

  constructor(id: string, companyId: string, supplierId: string, subscriptionStatus: SubscriptionStatus, createdAt: Date) {
    this.id = id;
    this.companyId = companyId;
    this.supplierId = supplierId;
    this.subscriptionStatus = subscriptionStatus;
    this.createdAt = createdAt;
  }
}

export class Company {
  public id: string
  public organizationDetailsId: string
  public subscriptionsIds: number[]
  public subscriptionsRequestsIds: number[]
  public preferencesId: number

  constructor(id: string, organizationDetailsId: string, subscriptionsIds: number[], subscriptionsRequestsIds: number[], preferencesId: number) {
    this.id = id;
    this.organizationDetailsId = organizationDetailsId;
    this.subscriptionsIds = subscriptionsIds;
    this.subscriptionsRequestsIds = subscriptionsRequestsIds;
    this.preferencesId = preferencesId;
  }
}

export class Company {
  public id: string
  public organizationDetailsId: string
  public subscriptionsIds: string[]
  public subscriptionsRequestsIds: string[]
  public preferencesId: string


  constructor(id: string, organizationDetailsId: string, subscriptionsIds: string[], subscriptionsRequestsIds: string[], preferencesId: string) {
    this.id = id;
    this.organizationDetailsId = organizationDetailsId;
    this.subscriptionsIds = subscriptionsIds;
    this.subscriptionsRequestsIds = subscriptionsRequestsIds;
    this.preferencesId = preferencesId;
  }
}

export class Supplier {
  public id: string
  public organizationDetailsId: string
  public websiteUrl: string
  public menuUrl: string
  public subscribersIds: number[]
  public subscriptionsRequestsIds: number[]
  public menuId: number
  public preferencesId: number

  constructor(id: string, organizationDetailsId: string, websiteUrl: string, menuUrl: string, subscribersIds: number[], subscriptionsRequestsIds: number[], menuId: number, preferencesId: number) {
    this.id = id;
    this.organizationDetailsId = organizationDetailsId;
    this.websiteUrl = websiteUrl;
    this.menuUrl = menuUrl;
    this.subscribersIds = subscribersIds;
    this.subscriptionsRequestsIds = subscriptionsRequestsIds;
    this.menuId = menuId;
    this.preferencesId = preferencesId;
  }
}

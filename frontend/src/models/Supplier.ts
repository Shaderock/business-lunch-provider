export class Supplier {
  public id: number
  public name: string
  public description: string
  public email: string
  public phone: string
  public logo: Uint8Array
  public usersIds: number[]
  public usersRequestsIds: number[]
  public websiteUrl: string
  public menuUrl: string
  public subscribersIds: number[]
  public subscriptionsRequestsIds: number[]
  public menuId: number
  public preferencesId: number


  constructor(id: number,
              name: string,
              description: string,
              email: string,
              phone: string,
              logo: Uint8Array,
              usersIds: number[],
              usersRequestsIds: number[],
              websiteUrl: string,
              menuUrl: string,
              subscribersIds: number[],
              subscriptionsRequestsIds: number[],
              menuId: number,
              preferencesId: number) {
    this.id = id;
    this.name = name;
    this.description = description;
    this.email = email;
    this.phone = phone;
    this.logo = logo;
    this.usersIds = usersIds;
    this.usersRequestsIds = usersRequestsIds;
    this.websiteUrl = websiteUrl;
    this.menuUrl = menuUrl;
    this.subscribersIds = subscribersIds;
    this.subscriptionsRequestsIds = subscriptionsRequestsIds;
    this.menuId = menuId;
    this.preferencesId = preferencesId;
  }
}

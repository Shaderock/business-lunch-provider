export class Company {
  public id: number
  public name: string
  public description: string
  public email: string
  public phone: string
  public logo: Uint8Array
  public usersIds: number[]
  public usersRequestsIds: number[]
  public subscriptionsIds: number[]
  public subscriptionsRequestsIds: number[]
  public preferencesId: number


  constructor(id: number,
              name: string,
              description: string,
              email: string,
              phone: string,
              logo: Uint8Array,
              usersIds: number[],
              usersRequestsIds: number[],
              subscriptionsIds: number[],
              subscriptionsRequestsIds: number[],
              preferencesId: number) {
    this.id = id;
    this.name = name;
    this.description = description;
    this.email = email;
    this.phone = phone;
    this.logo = logo;
    this.usersIds = usersIds;
    this.usersRequestsIds = usersRequestsIds;
    this.subscriptionsIds = subscriptionsIds;
    this.subscriptionsRequestsIds = subscriptionsRequestsIds;
    this.preferencesId = preferencesId;
  }
}

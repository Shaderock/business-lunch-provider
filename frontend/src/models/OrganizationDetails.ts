export class OrganizationDetails {
  public id: string
  public name: string
  public description: string
  public email: string
  public phone: string
  public usersIds: number[]
  public usersRequestsIds: number[]

  constructor(id: string, name: string, description: string, email: string, phone: string, usersIds: number[], usersRequestsIds: number[]) {
    this.id = id;
    this.name = name;
    this.description = description;
    this.email = email;
    this.phone = phone;
    this.usersIds = usersIds;
    this.usersRequestsIds = usersRequestsIds;
  }
}

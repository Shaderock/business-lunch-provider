export class OrganizationDetails {
  public id: string | null
  public name: string
  public description: string
  public email: string
  public phone: string
  public usersIds: number[] | null

  constructor(id: string | null,
              name: string,
              description: string,
              email: string,
              phone: string,
              usersIds: number[] | null) {
    this.id = id;
    this.name = name;
    this.description = description;
    this.email = email;
    this.phone = phone;
    this.usersIds = usersIds;
  }
}

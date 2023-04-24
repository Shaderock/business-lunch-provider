export class PublicOrganizationDetails {
  public id: string
  public name: string
  public description: string
  public email: string
  public phone: string

  constructor(id: string, name: string, description: string, email: string, phone: string) {
    this.id = id;
    this.name = name;
    this.description = description;
    this.email = email;
    this.phone = phone;
  }
}

export class Invitation {
  public id: string
  public createdAt: Date
  public companyId: string
  public userEmail: string
  public formattedCreatedAt: string

  constructor(id: string, createdAt: Date, companyId: string, userEmail: string, formattedCreatedAt: string) {
    this.id = id;
    this.createdAt = createdAt;
    this.companyId = companyId;
    this.userEmail = userEmail;
    this.formattedCreatedAt = formattedCreatedAt;
  }
}

import {Role} from "./Role";

export class User {
  public id: number
  public detailsId: string
  public organizationId: string
  public organizationRequestId: string
  public preferencesId: Role[]


  constructor(id: number,
              detailsId: string,
              organizationId: string,
              organizationRequestId: string,
              preferencesId: Role[]) {
    this.id = id;
    this.detailsId = detailsId;
    this.organizationId = organizationId;
    this.organizationRequestId = organizationRequestId;
    this.preferencesId = preferencesId;
  }
}

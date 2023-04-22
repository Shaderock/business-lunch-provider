import {Role} from "./Role";

export class UserDetails {
  public id: string | null
  public appUserId: number
  public email: string
  public firstName: string
  public lastName: string
  public roles: Role[]

  constructor(id: string | null,
              appUserId: number,
              email: string,
              firstName: string,
              lastName: string,
              roles: Role[]) {
    this.id = id
    this.appUserId = appUserId
    this.email = email
    this.firstName = firstName
    this.lastName = lastName
    this.roles = roles
  }
}

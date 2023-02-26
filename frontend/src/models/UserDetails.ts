import {Role} from "./Role";

export class UserDetails {
  public id: number
  public appUserId: number
  public email: string
  public firstName: string
  public lastName: string
  public roles: Role[]

  constructor(id: number,
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

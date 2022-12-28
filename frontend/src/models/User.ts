import {Role} from "./Role";

export class User {
    public id: number
    public email: string
    public firstName: string
    public lastName: string
    public roles: Role[]

    constructor(id: number, email: string, firstName: string, lastName: string, roles: Role[]) {
        this.id = id;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.roles = roles;
    }
}

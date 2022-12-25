export class User {
    public email: string
    public firstName: string
    public lastName: string
    public token: string
    public roles: string[]

    constructor(email: string, firstName: string, lastName: string, token: string, roles: string[]) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.token = token;
        this.roles = roles;
    }
}

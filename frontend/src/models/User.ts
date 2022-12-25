export class User {
    get lastName(): string {
        return this._lastName;
    }

    set lastName(value: string) {
        this._lastName = value;
    }

    get firstName(): string {
        return this._firstName;
    }

    set firstName(value: string) {
        this._firstName = value;
    }

    private _email: string
    private _firstName: string
    private _lastName: string
    private _token: string
    private _roles: string[]

    constructor(email: string, firstName: string, secondName: string, token: string, roles: string[]) {
        this._email = email;
        this._firstName = firstName;
        this._lastName = secondName;
        this._token = token;
        this._roles = roles;
    }

    get email(): string {
        return this._email;
    }

    set email(value: string) {
        this._email = value;
    }

    get token(): string {
        return this._token;
    }

    set token(value: string) {
        this._token = value;
    }

    get roles(): string[] {
        return this._roles;
    }

    set roles(value: string[]) {
        this._roles = value;
    }
}

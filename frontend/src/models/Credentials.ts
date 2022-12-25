export class Credentials {
    private _email: string
    private _password: string

    constructor(email: string, username: string) {
        this._email = email;
        this._password = username;
    }

    get email(): string {
        return this._email;
    }

    set email(value: string) {
        this._email = value;
    }

    get password(): string {
        return this._password;
    }

    set password(value: string) {
        this._password = value;
    }
}

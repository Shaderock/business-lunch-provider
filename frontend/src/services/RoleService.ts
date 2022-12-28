import {User} from "../models/User";
import {Role} from "../models/Role";

export class RoleService {
    hasRole(user: User | null, role: Role): boolean {
        return user !== null && user.roles !== null && user.roles.includes(role);
    }

    hasOnlyRole(user: User | null, role: Role): boolean {
        return this.hasRole(user, role) && user?.roles.length === 1;
    }
}

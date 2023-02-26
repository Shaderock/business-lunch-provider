import {UserDetails} from "@/models/UserDetails";
import {Role} from "@/models/Role";

export class RoleService {
  hasRole(user: UserDetails | null, role: Role): boolean {
    return user !== null && user.roles !== null && user.roles.includes(role);
  }

  hasOnlyRole(user: UserDetails | null, role: Role): boolean {
    return this.hasRole(user, role) && user?.roles.length === 1;
  }
}

const roleService = new RoleService()
export default roleService

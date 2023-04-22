import {UserDetails} from "@/models/UserDetails";
import {Role} from "@/models/Role";

export class RoleService {
  hasRole(user: UserDetails | null, role: Role): boolean {
    return user?.roles?.includes(role) ?? false;
  }

  hasOnlyRole(user: UserDetails | null, role: Role): boolean {
    return this.hasRole(user, role) && user?.roles.length === 1;
  }

  hasRoles(userDetails: UserDetails | null, roles: Role[]) {
    if (!userDetails) return false;
    return (
      userDetails.roles.length === roles.length &&
      roles.every(role => userDetails.roles.includes(role))
    );
  }

}

const roleService = new RoleService()
export default roleService

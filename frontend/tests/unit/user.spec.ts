import {beforeEach, describe, expect, it} from "vitest";
import {useAuthStore, useProfileStore} from "../../src/store/user-app";
import {createPinia, setActivePinia} from "pinia";
import {UserDetails} from "../../src/models/UserDetails";
import {Role} from "../../src/models/Role";

describe('UserDetails Storage and Handling', () => {
  const token: string = "asdf"
  const userDetails: UserDetails = {
    id: 1,
    appUserId: 2,
    email: "testmail@mail.mail.mail",
    firstName: "firstName",
    lastName: "lastName",
    roles: []
  }

  beforeEach(() => {
    setActivePinia(createPinia())
    useAuthStore().jwtToken = token
    useProfileStore().userDetails = userDetails
  })

  it('correctly validates user has token', async () => {
    expect(useAuthStore().hasToken).toBe(true)
  });

  it('correctly validates user is authenticated', async () => {
    expect(useAuthStore().isAuthenticated).toBe(true)
  });

  it('correctly validates user is not an employee', async () => {
    expect(useProfileStore().isEmployee).toBe(false)
  });

  it('correctly validates user is an employee', async () => {
    useProfileStore().user.roles.push(Role.Employee)
    expect(useProfileStore().isEmployee).toBe(true)
  });

  it('clears user data', async () => {
    useProfileStore().clearUser()
    expect(useProfileStore().hasUser).toBe(false)
  });
})

import {beforeEach, describe, expect, it} from "vitest";
import {useAuthStore, useUserStore} from "../../src/store/app";
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
    useUserStore().userDetails = userDetails
  })

  it('correctly validates user has token', async () => {
    expect(useAuthStore().hasToken).toBe(true)
  });

  it('correctly validates user is authenticated', async () => {
    expect(useAuthStore().isAuthenticated).toBe(true)
  });

  it('correctly validates user is not an employee', async () => {
    expect(useUserStore().isEmployee).toBe(false)
  });

  it('correctly validates user is an employee', async () => {
    useUserStore().user.roles.push(Role.Employee)
    expect(useUserStore().isEmployee).toBe(true)
  });

  it('clears user data', async () => {
    useUserStore().clearUser()
    expect(useUserStore().hasUser).toBe(false)
  });
})

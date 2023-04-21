import {defineStore} from "pinia";
import {OrganizationDetails} from "@/models/OrganizationDetails";
import organizationService from "@/services/OrganizationService";

export const useOrganizationStore = defineStore('organization', {
  state: () => ({
    organization: null as OrganizationDetails | null
  }),
  getters: {
    getOrganization(): OrganizationDetails | null {
      return this.organization
    },
    hasOrganization(): boolean {
      return this.organization !== null;
    },
  },
  actions: {
    clearOrganization() {
      this.organization = null
    },
    async requestFreshOrganizationData() {
      const response = await organizationService.getUserOrganization()
      this.organization = response.data
    }
  }
})

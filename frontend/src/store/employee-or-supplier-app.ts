import {defineStore} from "pinia";
import {OrganizationDetails} from "@/models/OrganizationDetails";
import organizationService from "@/services/OrganizationService";

export const useOrganizationStore = defineStore('organization', {
  state: () => ({
    organization: new OrganizationDetails(null, "", "", "", "", [])
  }),
  getters: {
    getOrganization(): OrganizationDetails {
      return this.organization
    },
    hasOrganization(): boolean {
      return this.organization !== null;
    },
    areDetailsCompleted(): boolean {
      if (!this.organization) return false
      return !(!this.organization.name ||
        !this.organization.email ||
        !this.organization.phone ||
        !this.organization.description);
    }
  },
  actions: {
    async requestFreshOrganizationData() {
      const response = await organizationService.getUserOrganization()
      this.organization = response.data
    }
  }
})

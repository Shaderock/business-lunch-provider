import {defineStore} from "pinia";
import {OrganizationDetails} from "@/models/OrganizationDetails";
import organizationService from "@/services/OrganizationService";

export const useOrganizationStore = defineStore('organization', {
  state: () => ({
    organization: new OrganizationDetails(null, "", "", "", "", []),
    logo: '',
    logoThumbnail: '',
  }),
  getters: {
    getOrganization(): OrganizationDetails {
      return this.organization
    },
    getLogoAsBase64(): string {
      return this.logo
    },
    getLogoAsBase64Thumbnail(): string {
      return this.logoThumbnail
    },
    hasLogo(): boolean {
      return this.logoThumbnail !== ''
    },
    getLogoCardWidth(): number {
      return 352
    },
    getLogoCardMaxHeight(): number {
      return 198
    },
    hasOrganization(): boolean {
      return this.organization !== null;
    },
    areDetailsCompleted(): boolean {
      if (!this.organization) return false
      if (!this.logo) return false
      if (this.logo === '') return false
      return !(!this.organization.name ||
        !this.organization.email ||
        !this.organization.phone ||
        !this.organization.description);
    }
  },
  actions: {
    async requestFreshOrganizationData(): Promise<void> {
      const response = await organizationService.getUserOrganization()
      this.organization = response.data
    },
    async requestLogo(): Promise<void> {
      this.logoThumbnail = await organizationService.requestUserOrganizationLogo(true)
      this.logo = await organizationService.requestUserOrganizationLogo(false)
    },
    async updateLogo(file: File): Promise<void> {
      await organizationService.updateOrganizationLogo(file)
      await this.requestLogo()
    }
  }
})

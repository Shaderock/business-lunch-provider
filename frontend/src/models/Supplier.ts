export class Supplier {
  public id: string | null
  public organizationDetailsId: string | null
  public websiteUrl: string
  public menuUrl: string
  public isPublic: boolean | null
  public preferencesId: string | null

  constructor(id: string | null, organizationDetailsId: string | null, websiteUrl: string, menuUrl: string, isPublic: boolean | null, preferencesId: string | null) {
    this.id = id;
    this.organizationDetailsId = organizationDetailsId;
    this.websiteUrl = websiteUrl;
    this.menuUrl = menuUrl;
    this.isPublic = isPublic;
    this.preferencesId = preferencesId;
  }
}

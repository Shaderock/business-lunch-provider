export class Category {
  id: string
  name: string
  optionIds: string[]
  createdAt: Date
  publishedAt: Date
  isPublic: boolean

  constructor(id: string, name: string, optionIds: string[], createdAt: Date, publishedAt: Date, isPublic: boolean) {
    this.id = id;
    this.name = name;
    this.optionIds = optionIds;
    this.createdAt = createdAt;
    this.publishedAt = publishedAt;
    this.isPublic = isPublic;
  }
}

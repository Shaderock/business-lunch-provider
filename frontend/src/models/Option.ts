export class Option {
  id: string
  name: string
  categoryId: string
  description: string
  price: number
  hasPhoto: boolean
  gram: string
  isPublic: boolean
  createdAt: Date
  publishedAt: Date

  constructor(id: string, name: string, categoryId: string, description: string, price: number, hasPhoto: boolean, gram: string, isPublic: boolean, createdAt: Date, publishedAt: Date) {
    this.id = id;
    this.name = name;
    this.categoryId = categoryId;
    this.description = description;
    this.price = price;
    this.hasPhoto = hasPhoto;
    this.gram = gram;
    this.isPublic = isPublic;
    this.createdAt = createdAt;
    this.publishedAt = publishedAt;
  }
}

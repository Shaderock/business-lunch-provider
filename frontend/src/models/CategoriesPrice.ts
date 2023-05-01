export class CategoriesPrice {
  id: string
  amount: number
  price: number

  constructor(id: string, amount: number, price: number) {
    this.id = id;
    this.amount = amount;
    this.price = price;
  }
}

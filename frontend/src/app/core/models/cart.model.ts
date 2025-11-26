export interface CartItem {
  productId: number;
  productName: string;
  productSku: string;
  price: number;
  quantity: number;
  imageUrl?: string;
}

export interface Cart {
  items: CartItem[];
  totalAmount: number;
  itemCount: number;
}

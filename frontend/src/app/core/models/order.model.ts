export enum OrderStatus {
  PENDING = 'PENDING',
  CONFIRMED = 'CONFIRMED',
  PROCESSING = 'PROCESSING',
  SHIPPED = 'SHIPPED',
  DELIVERED = 'DELIVERED',
  CANCELLED = 'CANCELLED',
  REFUNDED = 'REFUNDED'
}

export interface OrderItem {
  id?: number;
  orderId?: number;
  productId: number;
  productName: string;
  productSku: string;
  quantity: number;
  unitPrice: number;
  totalPrice?: number;
}

export interface Order {
  id: number;
  orderNumber: string;
  userId: number;
  status: OrderStatus;
  totalAmount: number;
  taxAmount: number;
  shippingAmount: number;
  shippingAddress: string;
  billingAddress: string;
  items: OrderItem[];
  createdAt?: Date;
  updatedAt?: Date;
}

export interface OrderCreate {
  userId: number;
  shippingAddress: string;
  billingAddress: string;
  items: OrderItem[];
  taxAmount: number;
  shippingAmount: number;
}

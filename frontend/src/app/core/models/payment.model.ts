export enum PaymentStatus {
  PENDING = 'PENDING',
  PROCESSING = 'PROCESSING',
  COMPLETED = 'COMPLETED',
  FAILED = 'FAILED',
  REFUNDED = 'REFUNDED',
  CANCELLED = 'CANCELLED'
}

export enum PaymentMethod {
  CREDIT_CARD = 'CREDIT_CARD',
  DEBIT_CARD = 'DEBIT_CARD',
  PAYPAL = 'PAYPAL',
  BANK_TRANSFER = 'BANK_TRANSFER',
  CASH_ON_DELIVERY = 'CASH_ON_DELIVERY',
  WALLET = 'WALLET'
}

export interface Payment {
  id: number;
  transactionId: string;
  orderId: number;
  userId: number;
  amount: number;
  paymentMethod: PaymentMethod;
  status: PaymentStatus;
  createdAt?: Date;
  updatedAt?: Date;
}

export interface PaymentCreate {
  orderId: number;
  userId: number;
  amount: number;
  paymentMethod: PaymentMethod;
}

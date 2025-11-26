export interface Inventory {
  id: number;
  productId: number;
  productName?: string;
  quantityAvailable: number;
  quantityReserved: number;
  reorderLevel: number;
  reorderQuantity: number;
  lastRestocked?: Date;
  createdAt?: Date;
  updatedAt?: Date;
}

export interface InventoryCreate {
  productId: number;
  quantityAvailable: number;
  quantityReserved: number;
  reorderLevel: number;
  reorderQuantity: number;
}

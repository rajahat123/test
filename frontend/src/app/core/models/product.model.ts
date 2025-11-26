export enum ProductStatus {
  ACTIVE = 'ACTIVE',
  INACTIVE = 'INACTIVE',
  OUT_OF_STOCK = 'OUT_OF_STOCK',
  DISCONTINUED = 'DISCONTINUED'
}

export interface Product {
  id: number;
  name: string;
  description: string;
  sku: string;
  price: number;
  categoryId: number;
  categoryName?: string;
  brand: string;
  imageUrl?: string;
  status: ProductStatus;
  createdAt?: Date;
  updatedAt?: Date;
}

export interface ProductCreate {
  name: string;
  description: string;
  sku: string;
  price: number;
  categoryId: number;
  brand: string;
  imageUrl?: string;
  status: ProductStatus;
}

export interface Category {
  id: number;
  name: string;
  description?: string;
  parentId?: number;
  createdAt?: Date;
  updatedAt?: Date;
}

export interface CategoryCreate {
  name: string;
  description?: string;
  parentId?: number;
}

export interface Review {
  id: number;
  productId: number;
  userId: number;
  userName?: string;
  rating: number;
  comment?: string;
  createdAt?: Date;
  updatedAt?: Date;
}

export interface ReviewCreate {
  productId: number;
  userId: number;
  rating: number;
  comment?: string;
}

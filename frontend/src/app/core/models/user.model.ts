export enum UserStatus {
  ACTIVE = 'ACTIVE',
  INACTIVE = 'INACTIVE',
  SUSPENDED = 'SUSPENDED',
  PENDING_VERIFICATION = 'PENDING_VERIFICATION'
}

export enum UserRole {
  CUSTOMER = 'CUSTOMER',
  ADMIN = 'ADMIN',
  VENDOR = 'VENDOR',
  SUPPORT = 'SUPPORT'
}

export interface User {
  id: number;
  email: string;
  firstName: string;
  lastName: string;
  phoneNumber: string;
  status: UserStatus;
  role: UserRole;
  createdAt?: Date;
  updatedAt?: Date;
}

export interface UserRegistration {
  email: string;
  password: string;
  firstName: string;
  lastName: string;
  phoneNumber: string;
}

export interface UserLogin {
  email: string;
  password: string;
}

export interface LoginResponse {
  token: string;
  user: User;
}

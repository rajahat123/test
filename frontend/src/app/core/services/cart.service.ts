import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { Cart, CartItem } from '../models/cart.model';

@Injectable({
  providedIn: 'root'
})
export class CartService {
  private cartKey = 'shopping_cart';
  private cartSubject = new BehaviorSubject<Cart>(this.loadCart());
  public cart$ = this.cartSubject.asObservable();

  constructor() {}

  private loadCart(): Cart {
    const cartStr = localStorage.getItem(this.cartKey);
    if (cartStr) {
      try {
        return JSON.parse(cartStr);
      } catch (e) {
        return this.createEmptyCart();
      }
    }
    return this.createEmptyCart();
  }

  private createEmptyCart(): Cart {
    return {
      items: [],
      totalAmount: 0,
      itemCount: 0
    };
  }

  private saveCart(cart: Cart): void {
    localStorage.setItem(this.cartKey, JSON.stringify(cart));
    this.cartSubject.next(cart);
  }

  private calculateTotals(cart: Cart): void {
    cart.itemCount = cart.items.reduce((sum, item) => sum + item.quantity, 0);
    cart.totalAmount = cart.items.reduce((sum, item) => sum + (item.price * item.quantity), 0);
  }

  addItem(item: CartItem): void {
    const cart = this.cartSubject.value;
    const existingItem = cart.items.find(i => i.productId === item.productId);

    if (existingItem) {
      existingItem.quantity += item.quantity;
    } else {
      cart.items.push(item);
    }

    this.calculateTotals(cart);
    this.saveCart(cart);
  }

  removeItem(productId: number): void {
    const cart = this.cartSubject.value;
    cart.items = cart.items.filter(item => item.productId !== productId);
    this.calculateTotals(cart);
    this.saveCart(cart);
  }

  updateItemQuantity(productId: number, quantity: number): void {
    const cart = this.cartSubject.value;
    const item = cart.items.find(i => i.productId === productId);

    if (item) {
      if (quantity <= 0) {
        this.removeItem(productId);
      } else {
        item.quantity = quantity;
        this.calculateTotals(cart);
        this.saveCart(cart);
      }
    }
  }

  clearCart(): void {
    this.saveCart(this.createEmptyCart());
  }

  getCart(): Cart {
    return this.cartSubject.value;
  }
}

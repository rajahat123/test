import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { CartService } from '../../../core/services/cart.service';
import { OrderService } from '../../../core/services/order.service';
import { PaymentService } from '../../../core/services/payment.service';
import { AuthService } from '../../../core/services/auth.service';
import { UserService } from '../../../core/services/user.service';
import { Address } from '../../../core/models/address.model';
import { OrderCreate } from '../../../core/models/order.model';
import { PaymentMethod } from '../../../core/models/payment.model';

@Component({
  selector: 'app-checkout',
  templateUrl: './checkout.component.html',
  styleUrls: ['./checkout.component.css']
})
export class CheckoutComponent implements OnInit {
  checkoutForm: FormGroup;
  addresses: Address[] = [];
  cart = this.cartService.getCart();
  loading = false;
  error = '';
  paymentMethods = Object.values(PaymentMethod);

  constructor(
    private fb: FormBuilder,
    private cartService: CartService,
    private orderService: OrderService,
    private paymentService: PaymentService,
    private authService: AuthService,
    private userService: UserService,
    private router: Router
  ) {
    this.checkoutForm = this.fb.group({
      shippingAddress: ['', Validators.required],
      billingAddress: ['', Validators.required],
      paymentMethod: [PaymentMethod.CREDIT_CARD, Validators.required]
    });
  }

  ngOnInit(): void {
    const currentUser = this.authService.getCurrentUser();
    if (!currentUser) {
      this.router.navigate(['/auth/login'], { queryParams: { returnUrl: '/cart/checkout' } });
      return;
    }

    if (this.cart.items.length === 0) {
      this.router.navigate(['/cart']);
      return;
    }

    this.loadAddresses(currentUser.id);
  }

  loadAddresses(userId: number): void {
    this.userService.getAddressesByUser(userId).subscribe({
      next: (addresses) => {
        this.addresses = addresses;
        if (addresses.length > 0) {
          const defaultAddress = addresses.find(a => a.isDefault) || addresses[0];
          const addressString = this.formatAddress(defaultAddress);
          this.checkoutForm.patchValue({
            shippingAddress: addressString,
            billingAddress: addressString
          });
        }
      },
      error: (error) => {
        console.error('Failed to load addresses:', error);
      }
    });
  }

  formatAddress(address: Address): string {
    return `${address.addressLine1}, ${address.city}, ${address.state} ${address.postalCode}, ${address.country}`;
  }

  get taxAmount(): number {
    return this.cart.totalAmount * 0.08;
  }

  get shippingAmount(): number {
    return 10.00;
  }

  get totalAmount(): number {
    return this.cart.totalAmount + this.taxAmount + this.shippingAmount;
  }

  placeOrder(): void {
    if (this.checkoutForm.invalid) {
      return;
    }

    const currentUser = this.authService.getCurrentUser();
    if (!currentUser) {
      return;
    }

    this.loading = true;
    this.error = '';

    const orderData: OrderCreate = {
      userId: currentUser.id,
      shippingAddress: this.checkoutForm.value.shippingAddress,
      billingAddress: this.checkoutForm.value.billingAddress,
      items: this.cart.items.map(item => ({
        productId: item.productId,
        productName: item.productName,
        productSku: item.productSku,
        quantity: item.quantity,
        unitPrice: item.price
      })),
      taxAmount: this.taxAmount,
      shippingAmount: this.shippingAmount
    };

    this.orderService.createOrder(orderData).subscribe({
      next: (order) => {
        // Process payment
        this.paymentService.processPayment({
          orderId: order.id,
          userId: currentUser.id,
          amount: this.totalAmount,
          paymentMethod: this.checkoutForm.value.paymentMethod
        }).subscribe({
          next: () => {
            this.cartService.clearCart();
            this.router.navigate(['/orders', order.id]);
          },
          error: (error) => {
            this.error = error.message || 'Payment failed';
            this.loading = false;
          }
        });
      },
      error: (error) => {
        this.error = error.message || 'Failed to place order';
        this.loading = false;
      }
    });
  }
}

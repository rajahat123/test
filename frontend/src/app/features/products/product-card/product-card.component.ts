import { Component, Input } from '@angular/core';
import { Router } from '@angular/router';
import { Product } from '../../../core/models/product.model';
import { CartService } from '../../../core/services/cart.service';

@Component({
  selector: 'app-product-card',
  templateUrl: './product-card.component.html',
  styleUrls: ['./product-card.component.css']
})
export class ProductCardComponent {
  @Input() product!: Product;

  constructor(
    private cartService: CartService,
    private router: Router
  ) {}

  addToCart(): void {
    this.cartService.addItem({
      productId: this.product.id,
      productName: this.product.name,
      productSku: this.product.sku,
      price: this.product.price,
      quantity: 1,
      imageUrl: this.product.imageUrl
    });
  }

  viewDetails(): void {
    this.router.navigate(['/products', this.product.id]);
  }
}

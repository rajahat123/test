import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ProductService } from '../../../core/services/product.service';
import { CartService } from '../../../core/services/cart.service';
import { AuthService } from '../../../core/services/auth.service';
import { InventoryService } from '../../../core/services/inventory.service';
import { Product, Review, ReviewCreate } from '../../../core/models/product.model';
import { Inventory } from '../../../core/models/inventory.model';

@Component({
  selector: 'app-product-detail',
  templateUrl: './product-detail.component.html',
  styleUrls: ['./product-detail.component.css']
})
export class ProductDetailComponent implements OnInit {
  product: Product | null = null;
  reviews: Review[] = [];
  inventory: Inventory | null = null;
  loading = false;
  error = '';
  quantity = 1;
  
  // Review form
  showReviewForm = false;
  newReview: ReviewCreate = {
    productId: 0,
    userId: 0,
    rating: 5,
    comment: ''
  };

  constructor(
    private route: ActivatedRoute,
    private productService: ProductService,
    private cartService: CartService,
    private authService: AuthService,
    private inventoryService: InventoryService
  ) {}

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    if (id) {
      this.loadProduct(id);
      this.loadReviews(id);
      this.loadInventory(id);
    }
  }

  loadProduct(id: number): void {
    this.loading = true;
    this.productService.getProductById(id).subscribe({
      next: (product) => {
        this.product = product;
        this.newReview.productId = product.id;
        this.loading = false;
      },
      error: (error) => {
        this.error = error.message || 'Failed to load product';
        this.loading = false;
      }
    });
  }

  loadReviews(productId: number): void {
    this.productService.getReviewsByProduct(productId).subscribe({
      next: (reviews) => {
        this.reviews = reviews;
      },
      error: (error) => {
        console.error('Failed to load reviews:', error);
      }
    });
  }

  loadInventory(productId: number): void {
    this.inventoryService.getInventoryByProduct(productId).subscribe({
      next: (inventory) => {
        this.inventory = inventory;
      },
      error: (error) => {
        console.error('Failed to load inventory:', error);
      }
    });
  }

  addToCart(): void {
    if (this.product) {
      this.cartService.addItem({
        productId: this.product.id,
        productName: this.product.name,
        productSku: this.product.sku,
        price: this.product.price,
        quantity: this.quantity,
        imageUrl: this.product.imageUrl
      });
    }
  }

  submitReview(): void {
    const currentUser = this.authService.getCurrentUser();
    if (!currentUser) {
      this.error = 'Please login to submit a review';
      return;
    }

    this.newReview.userId = currentUser.id;
    
    this.productService.createReview(this.newReview).subscribe({
      next: (review) => {
        this.reviews.unshift(review);
        this.showReviewForm = false;
        this.newReview.comment = '';
        this.newReview.rating = 5;
      },
      error: (error) => {
        this.error = error.message || 'Failed to submit review';
      }
    });
  }

  get averageRating(): number {
    if (this.reviews.length === 0) return 0;
    const sum = this.reviews.reduce((acc, review) => acc + review.rating, 0);
    return sum / this.reviews.length;
  }

  isLoggedIn(): boolean {
    return this.authService.isAuthenticated();
  }
}

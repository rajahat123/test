import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ProductService } from '../../../core/services/product.service';
import { Product, Category } from '../../../core/models/product.model';

@Component({
  selector: 'app-product-list',
  templateUrl: './product-list.component.html',
  styleUrls: ['./product-list.component.css']
})
export class ProductListComponent implements OnInit {
  products: Product[] = [];
  categories: Category[] = [];
  loading = false;
  error = '';
  selectedCategory: number | null = null;
  searchQuery = '';
  minPrice = 0;
  maxPrice = 10000;

  constructor(
    private productService: ProductService,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.loadCategories();
    
    this.route.queryParams.subscribe(params => {
      this.searchQuery = params['search'] || '';
      if (this.searchQuery) {
        this.searchProducts();
      } else {
        this.loadProducts();
      }
    });
  }

  loadCategories(): void {
    this.productService.getAllCategories().subscribe({
      next: (categories) => {
        this.categories = categories;
      },
      error: (error) => {
        console.error('Failed to load categories:', error);
      }
    });
  }

  loadProducts(): void {
    this.loading = true;
    this.error = '';

    this.productService.getAllProducts().subscribe({
      next: (products) => {
        this.products = products;
        this.loading = false;
      },
      error: (error) => {
        this.error = error.message || 'Failed to load products';
        this.loading = false;
      }
    });
  }

  filterByCategory(categoryId: number): void {
    this.selectedCategory = categoryId;
    this.loading = true;

    this.productService.getProductsByCategory(categoryId).subscribe({
      next: (products) => {
        this.products = products;
        this.loading = false;
      },
      error: (error) => {
        this.error = error.message || 'Failed to load products';
        this.loading = false;
      }
    });
  }

  searchProducts(): void {
    if (!this.searchQuery.trim()) {
      this.loadProducts();
      return;
    }

    this.loading = true;
    this.productService.searchProducts(this.searchQuery).subscribe({
      next: (products) => {
        this.products = products;
        this.loading = false;
      },
      error: (error) => {
        this.error = error.message || 'Search failed';
        this.loading = false;
      }
    });
  }

  filterByPrice(): void {
    this.loading = true;
    this.productService.getProductsByPriceRange(this.minPrice, this.maxPrice).subscribe({
      next: (products) => {
        this.products = products;
        this.loading = false;
      },
      error: (error) => {
        this.error = error.message || 'Failed to filter products';
        this.loading = false;
      }
    });
  }

  clearFilters(): void {
    this.selectedCategory = null;
    this.searchQuery = '';
    this.minPrice = 0;
    this.maxPrice = 10000;
    this.loadProducts();
  }
}

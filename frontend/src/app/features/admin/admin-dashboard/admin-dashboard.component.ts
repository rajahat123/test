import { Component, OnInit } from '@angular/core';
import { UserService } from '../../../core/services/user.service';
import { ProductService } from '../../../core/services/product.service';
import { OrderService } from '../../../core/services/order.service';
import { InventoryService } from '../../../core/services/inventory.service';
import { User } from '../../../core/models/user.model';
import { Product } from '../../../core/models/product.model';
import { OrderStatus } from '../../../core/models/order.model';
import { Inventory } from '../../../core/models/inventory.model';

@Component({
  selector: 'app-admin-dashboard',
  templateUrl: './admin-dashboard.component.html',
  styleUrls: ['./admin-dashboard.component.css']
})
export class AdminDashboardComponent implements OnInit {
  users: User[] = [];
  products: Product[] = [];
  lowStockItems: Inventory[] = [];
  pendingOrdersCount = 0;
  loading = false;

  constructor(
    private userService: UserService,
    private productService: ProductService,
    private orderService: OrderService,
    private inventoryService: InventoryService
  ) {}

  ngOnInit(): void {
    this.loadDashboardData();
  }

  loadDashboardData(): void {
    this.loading = true;

    // Load users
    this.userService.getAllUsers().subscribe({
      next: (users) => {
        this.users = users;
      },
      error: (error) => {
        console.error('Failed to load users:', error);
      }
    });

    // Load products
    this.productService.getAllProducts().subscribe({
      next: (products) => {
        this.products = products;
      },
      error: (error) => {
        console.error('Failed to load products:', error);
      }
    });

    // Load low stock items
    this.inventoryService.getLowStockItems().subscribe({
      next: (items) => {
        this.lowStockItems = items;
        this.loading = false;
      },
      error: (error) => {
        console.error('Failed to load inventory:', error);
        this.loading = false;
      }
    });

    // Load pending orders count
    this.orderService.getOrdersByStatus(OrderStatus.PENDING).subscribe({
      next: (orders) => {
        this.pendingOrdersCount = orders.length;
      },
      error: (error) => {
        console.error('Failed to load orders:', error);
      }
    });
  }

  get activeProducts(): number {
    return this.products.filter(p => p.status === 'ACTIVE').length;
  }

  get activeUsers(): number {
    return this.users.filter(u => u.status === 'ACTIVE').length;
  }
}

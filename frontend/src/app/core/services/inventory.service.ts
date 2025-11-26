import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { Inventory, InventoryCreate } from '../models/inventory.model';

@Injectable({
  providedIn: 'root'
})
export class InventoryService {
  private apiUrl = `${environment.apiUrl}/inventory`;

  constructor(private http: HttpClient) {}

  createInventory(inventory: InventoryCreate): Observable<Inventory> {
    return this.http.post<Inventory>(this.apiUrl, inventory);
  }

  getInventoryById(id: number): Observable<Inventory> {
    return this.http.get<Inventory>(`${this.apiUrl}/${id}`);
  }

  getInventoryByProduct(productId: number): Observable<Inventory> {
    return this.http.get<Inventory>(`${this.apiUrl}/product/${productId}`);
  }

  getAllInventory(): Observable<Inventory[]> {
    return this.http.get<Inventory[]>(this.apiUrl);
  }

  getLowStockItems(): Observable<Inventory[]> {
    return this.http.get<Inventory[]>(`${this.apiUrl}/low-stock`);
  }

  updateStock(productId: number, quantity: number): Observable<Inventory> {
    const params = new HttpParams().set('quantity', quantity.toString());
    return this.http.patch<Inventory>(`${this.apiUrl}/product/${productId}/stock`, null, { params });
  }

  reserveStock(productId: number, quantity: number): Observable<Inventory> {
    const params = new HttpParams().set('quantity', quantity.toString());
    return this.http.post<Inventory>(`${this.apiUrl}/product/${productId}/reserve`, null, { params });
  }

  releaseStock(productId: number, quantity: number): Observable<Inventory> {
    const params = new HttpParams().set('quantity', quantity.toString());
    return this.http.post<Inventory>(`${this.apiUrl}/product/${productId}/release`, null, { params });
  }

  deductStock(productId: number, quantity: number): Observable<Inventory> {
    const params = new HttpParams().set('quantity', quantity.toString());
    return this.http.post<Inventory>(`${this.apiUrl}/product/${productId}/deduct`, null, { params });
  }
}

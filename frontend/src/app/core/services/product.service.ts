import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { Product, ProductCreate, ProductStatus, Category, CategoryCreate, Review, ReviewCreate } from '../models/product.model';

@Injectable({
  providedIn: 'root'
})
export class ProductService {
  private apiUrl = `${environment.apiUrl}/products`;
  private categoryUrl = `${environment.apiUrl}/categories`;
  private reviewUrl = `${environment.apiUrl}/reviews`;

  constructor(private http: HttpClient) {}

  // Product Management APIs
  createProduct(product: ProductCreate): Observable<Product> {
    return this.http.post<Product>(this.apiUrl, product);
  }

  getProductById(id: number): Observable<Product> {
    return this.http.get<Product>(`${this.apiUrl}/${id}`);
  }

  getProductBySku(sku: string): Observable<Product> {
    return this.http.get<Product>(`${this.apiUrl}/sku/${sku}`);
  }

  getAllProducts(): Observable<Product[]> {
    return this.http.get<Product[]>(this.apiUrl);
  }

  getProductsByCategory(categoryId: number): Observable<Product[]> {
    return this.http.get<Product[]>(`${this.apiUrl}/category/${categoryId}`);
  }

  searchProducts(query: string): Observable<Product[]> {
    const params = new HttpParams().set('query', query);
    return this.http.get<Product[]>(`${this.apiUrl}/search`, { params });
  }

  getProductsByBrand(brand: string): Observable<Product[]> {
    return this.http.get<Product[]>(`${this.apiUrl}/brand/${brand}`);
  }

  getProductsByPriceRange(minPrice: number, maxPrice: number): Observable<Product[]> {
    const params = new HttpParams()
      .set('minPrice', minPrice.toString())
      .set('maxPrice', maxPrice.toString());
    return this.http.get<Product[]>(`${this.apiUrl}/price-range`, { params });
  }

  updateProduct(id: number, product: Partial<Product>): Observable<Product> {
    return this.http.put<Product>(`${this.apiUrl}/${id}`, product);
  }

  deleteProduct(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  updateProductStatus(id: number, status: ProductStatus): Observable<Product> {
    const params = new HttpParams().set('status', status);
    return this.http.patch<Product>(`${this.apiUrl}/${id}/status`, null, { params });
  }

  // Category Management APIs
  createCategory(category: CategoryCreate): Observable<Category> {
    return this.http.post<Category>(this.categoryUrl, category);
  }

  getCategoryById(id: number): Observable<Category> {
    return this.http.get<Category>(`${this.categoryUrl}/${id}`);
  }

  getAllCategories(): Observable<Category[]> {
    return this.http.get<Category[]>(this.categoryUrl);
  }

  getSubcategories(parentId: number): Observable<Category[]> {
    return this.http.get<Category[]>(`${this.categoryUrl}/${parentId}/subcategories`);
  }

  updateCategory(id: number, category: Partial<Category>): Observable<Category> {
    return this.http.put<Category>(`${this.categoryUrl}/${id}`, category);
  }

  deleteCategory(id: number): Observable<void> {
    return this.http.delete<void>(`${this.categoryUrl}/${id}`);
  }

  // Review Management APIs
  createReview(review: ReviewCreate): Observable<Review> {
    return this.http.post<Review>(this.reviewUrl, review);
  }

  getReviewById(id: number): Observable<Review> {
    return this.http.get<Review>(`${this.reviewUrl}/${id}`);
  }

  getReviewsByProduct(productId: number): Observable<Review[]> {
    return this.http.get<Review[]>(`${this.reviewUrl}/product/${productId}`);
  }

  getReviewsByUser(userId: number): Observable<Review[]> {
    return this.http.get<Review[]>(`${this.reviewUrl}/user/${userId}`);
  }

  deleteReview(id: number): Observable<void> {
    return this.http.delete<void>(`${this.reviewUrl}/${id}`);
  }
}

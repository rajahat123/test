import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { User, UserStatus } from '../models/user.model';
import { Address, AddressCreate, AddressType } from '../models/address.model';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private apiUrl = `${environment.apiUrl}/users`;
  private addressUrl = `${environment.apiUrl}/addresses`;

  constructor(private http: HttpClient) {}

  // User Management APIs
  getUserById(id: number): Observable<User> {
    return this.http.get<User>(`${this.apiUrl}/${id}`);
  }

  getUserByEmail(email: string): Observable<User> {
    return this.http.get<User>(`${this.apiUrl}/email/${email}`);
  }

  getAllUsers(): Observable<User[]> {
    return this.http.get<User[]>(this.apiUrl);
  }

  searchUsers(query: string): Observable<User[]> {
    const params = new HttpParams().set('query', query);
    return this.http.get<User[]>(`${this.apiUrl}/search`, { params });
  }

  updateUser(id: number, userData: Partial<User>): Observable<User> {
    return this.http.put<User>(`${this.apiUrl}/${id}`, userData);
  }

  deleteUser(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  updateUserStatus(id: number, status: UserStatus): Observable<User> {
    const params = new HttpParams().set('status', status);
    return this.http.patch<User>(`${this.apiUrl}/${id}/status`, null, { params });
  }

  // Address Management APIs
  createAddress(address: AddressCreate): Observable<Address> {
    return this.http.post<Address>(this.addressUrl, address);
  }

  getAddressById(id: number): Observable<Address> {
    return this.http.get<Address>(`${this.addressUrl}/${id}`);
  }

  getAddressesByUser(userId: number): Observable<Address[]> {
    return this.http.get<Address[]>(`${this.addressUrl}/user/${userId}`);
  }

  updateAddress(id: number, address: Partial<Address>): Observable<Address> {
    return this.http.put<Address>(`${this.addressUrl}/${id}`, address);
  }

  deleteAddress(id: number): Observable<void> {
    return this.http.delete<void>(`${this.addressUrl}/${id}`);
  }

  setDefaultAddress(addressId: number, userId: number): Observable<Address> {
    const params = new HttpParams().set('userId', userId.toString());
    return this.http.patch<Address>(`${this.addressUrl}/${addressId}/default`, null, { params });
  }
}

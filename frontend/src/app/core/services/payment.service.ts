import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { Payment, PaymentCreate } from '../models/payment.model';

@Injectable({
  providedIn: 'root'
})
export class PaymentService {
  private apiUrl = `${environment.apiUrl}/payments`;

  constructor(private http: HttpClient) {}

  processPayment(payment: PaymentCreate): Observable<Payment> {
    return this.http.post<Payment>(this.apiUrl, payment);
  }

  getPaymentById(id: number): Observable<Payment> {
    return this.http.get<Payment>(`${this.apiUrl}/${id}`);
  }

  getPaymentByTransactionId(transactionId: string): Observable<Payment> {
    return this.http.get<Payment>(`${this.apiUrl}/transaction/${transactionId}`);
  }

  getPaymentsByOrder(orderId: number): Observable<Payment[]> {
    return this.http.get<Payment[]>(`${this.apiUrl}/order/${orderId}`);
  }

  getPaymentsByUser(userId: number): Observable<Payment[]> {
    return this.http.get<Payment[]>(`${this.apiUrl}/user/${userId}`);
  }

  refundPayment(id: number): Observable<Payment> {
    return this.http.post<Payment>(`${this.apiUrl}/${id}/refund`, null);
  }
}

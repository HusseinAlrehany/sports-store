import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { StorageService } from '../../../services/local-storage/storage.service';

const baseUrl = "http://localhost:8080/";

@Injectable({
  providedIn: 'root'
})
export class AdminService {

  constructor(private httpClient: HttpClient) { }


  private createAuthorizationHeader(): HttpHeaders {
    return new HttpHeaders().set(
      "Authorization", "Bearer " + StorageService.getToken()
    )
  }

  addCategory(CategoryDTO: any): Observable<any> {
     return this.httpClient.post(baseUrl + "api/admin/category", CategoryDTO , {
        headers: this.createAuthorizationHeader(),
     })

  }

  addProduct(productDTO: any): Observable<any> {
    return this.httpClient.post(baseUrl + "api/admin/product", productDTO, {
      headers: this.createAuthorizationHeader(),
    })
  }
  updateProduct(productId: number, productDto: any): Observable<any> {
    return this.httpClient.put(baseUrl + `api/admin/product/${productId}`, productDto, {
      headers: this.createAuthorizationHeader(),
    })
  }

  getProductById(productId: number): Observable<any> {
    return this.httpClient.get(baseUrl + `api/admin/product/${productId}`, {
      headers: this.createAuthorizationHeader(), 
    })
  }

  getAllCategories(): Observable<any> {
    return this.httpClient.get(baseUrl + "api/admin/categories", {
      headers: this.createAuthorizationHeader(),
    })
  }

  getAllProducts(): Observable<any> {
    return this.httpClient.get(baseUrl + "api/admin/products", {
      headers: this.createAuthorizationHeader(),
    })
  }

  getAllProductsByName(title: string): Observable<any> {
    
      return this.httpClient.get(baseUrl + `api/admin/search/${title}`, {
        headers: this.createAuthorizationHeader(),
      })
  }
  deleteProductById(id: number): Observable<any> {
    return this.httpClient.delete(baseUrl + `api/admin/product/${id}`, {
      headers: this.createAuthorizationHeader(),
    })
  }

  createCoupon(couponDTO: any): Observable<any> {
    return this.httpClient.post(baseUrl + `api/admin/coupons`, couponDTO, {
      headers: this.createAuthorizationHeader(),
    })
  }

  getAllCoupons(): Observable<any> {
    return this.httpClient.get(baseUrl + `api/admin/coupons`, {
      headers: this.createAuthorizationHeader(),
    })
  }
  getAllOrders(): Observable<any> {
    return this.httpClient.get(baseUrl + `api/admin/orders`, {
      headers: this.createAuthorizationHeader(),
    })
  }
  changeOrderStatus(orderId: number, status: string): Observable<any> {
       return this.httpClient.get(baseUrl + `api/admin/order/${orderId}/${status}`, {
        headers: this.createAuthorizationHeader(),
       })
  }

  createFaq(productId: number, faqDto: any): Observable<any> {
     return this.httpClient.post(baseUrl + `api/admin/faq/${productId}`, faqDto ,{
        headers: this.createAuthorizationHeader(), 
     })
  }

  getAnalytics(): Observable<any> {
    return this.httpClient.get(baseUrl + `api/admin/order/analytics`, {
      headers: this.createAuthorizationHeader(),
    })
  }

}

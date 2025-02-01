import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { StorageService } from '../../../services/local-storage/storage.service';

@Injectable({
  providedIn: 'root'
})
export class CustomerService {
 baseUrl = "http://localhost:8080/";

  constructor(private httpClient: HttpClient) { }

  private createAuthorizationHeader(): HttpHeaders {

    return new HttpHeaders().set(
      "Authorization", "Bearer " + StorageService.getToken()
    )
  }

  getAllProducts(): Observable<any> {   
    return this.httpClient.get(this.baseUrl + "api/customer/products", {
      headers: this.createAuthorizationHeader(),
    })
  }

  getAllProductsByName(title: string): Observable<any> {
    return this.httpClient.get(this.baseUrl + `api/customer/search/${title}`, {
      headers: this.createAuthorizationHeader(),
    })
  }

  addToCart(productId: any): Observable<any> {
      const cartDTO = {
        productId: productId,
        userId: StorageService.getUserId()
      }
      return this.httpClient.post(this.baseUrl + `api/customer/cart`, cartDTO ,{
        headers: this.createAuthorizationHeader(),
      } )
  }

  placeOrder(orderDTO: any): Observable<any> {
    orderDTO.userId = StorageService.getUserId();
    return this.httpClient.post(this.baseUrl + `api/customer/placeOrder`, orderDTO, {
      headers: this.createAuthorizationHeader(),
    })
  }

  getOrderSummary(): Observable<any> {
    const userId = StorageService.getUserId();
    return this.httpClient.get(this.baseUrl + `api/customer/order-summary/${userId}`, {
      headers: this.createAuthorizationHeader(),
    })
  }

  increaseProductQuantity(productId: any): Observable<any> {
     const cartDTO= {
      productId: productId,
      userId: StorageService.getUserId()
     }
     return this.httpClient.post(this.baseUrl + `api/customer/increase-quantity`, cartDTO , {
       headers: this.createAuthorizationHeader(),
     })
    
  }  

  decreaseProductQuantity(productId: any): Observable<any> {
    const cartDTO = {
      productId: productId,
      userId: StorageService.getUserId()
    }

    return this.httpClient.post(this.baseUrl + `api/customer/decrease-quantity`, cartDTO, {
      headers: this.createAuthorizationHeader(),
    })
  }

  getCartByUserId(): Observable<any> {
    const userId = StorageService.getUserId();
    return this.httpClient.get(this.baseUrl + `api/customer/cart/${userId}`, {
      headers: this.createAuthorizationHeader(),
    })
  }

  clearCartByUserId(): Observable<any> {
    const userId = StorageService.getUserId();
    return this.httpClient.delete(this.baseUrl + `api/customer/cart/${userId}`, {
      headers: this.createAuthorizationHeader(),
    })
  }

  deleteCartItemById(itemId: number): Observable<any> {
    return this.httpClient.delete(this.baseUrl + `api/customer/cart-item/${itemId}`, {
      headers: this.createAuthorizationHeader(),
    })
  }

  applyCoupon(code:string): Observable<any> {
     const userId = StorageService.getUserId();
     return this.httpClient.get(this.baseUrl + `api/customer/coupon/${userId}/${code}`, {
      headers: this.createAuthorizationHeader(),
     })
  }

  getOrdersByUserId(): Observable<any> {
     const userId = StorageService.getUserId();
     return this.httpClient.get(this.baseUrl + `api/customer/myOrders/${userId}`, {
      headers: this.createAuthorizationHeader(),

     })  
  }

  getOrderedProducts(orderId: number): Observable<any> {
    return this.httpClient.get(this.baseUrl + `api/customer/ordered-products/${orderId}`, {
      headers: this.createAuthorizationHeader(),
    })
  }

  createReview(reviewDto: any): Observable<any> {
    return this.httpClient.post(this.baseUrl + `api/customer/review`, reviewDto , {
      headers: this.createAuthorizationHeader(),
    })
  }

  getProductDetails(productId: number): Observable<any> {
    return this.httpClient.get(this.baseUrl + `api/customer/products/${productId}`, {
      headers: this.createAuthorizationHeader(),
    })
  }


  updateUserProfile(userProfileDTO: any): Observable<any> {
    
    const userId = StorageService.getUserId();
    return this.httpClient.put(this.baseUrl + `api/customer/edit-profile/${userId}`, userProfileDTO , {
      headers: this.createAuthorizationHeader(),
    })
  }

  getUserById(): Observable<any> {
    const userId = StorageService.getUserId();
    return this.httpClient.get(this.baseUrl + `api/customer/getUser/${userId}`, {
      headers: this.createAuthorizationHeader(),
    })
  }
}

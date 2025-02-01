import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { map, Observable } from 'rxjs';
import { StorageService } from '../local-storage/storage.service';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  baseUrl = "http://localhost:8080/";

  constructor(private httpClient: HttpClient) { }

  signup(signupRequest: any ): Observable<any> {
     return this.httpClient.post(this.baseUrl + "signup" , signupRequest);
  }

  signin(signinRequest: any): Observable<any> {
    return this.httpClient.post(this.baseUrl + "signin", signinRequest);
  }

getOrderByTrackingId(trackingId: number): Observable<any> {
  return this.httpClient.get( this.baseUrl + `orders/${trackingId}`);
}


}

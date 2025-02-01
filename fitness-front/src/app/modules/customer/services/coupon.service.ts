import { Injectable } from '@angular/core';
//coupon service is created to share and save the coupon code
//after clicking on apply coupon form
//and to send the coupon code in the payload of the request when placing the order
//because place order form did not have a field for coupon code

@Injectable({
  providedIn: 'root'
})
export class CouponService {

  constructor() { }


  private couponCode: string | null = null;

  setCouponCode(code: string) {
    this.couponCode = code;
  }

  getCouponCode(): string | null {
    return this.couponCode;
  }

  clearCouponCode() {
    this.couponCode = null;
  }

}

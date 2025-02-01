import { Component } from '@angular/core';
import { CustomerService } from '../../services/customer.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { CommonModule } from '@angular/common';
import { MaterialModule } from '../../../../Material.Module';
import { PlaceOrderComponent } from '../place-order/place-order.component';
import { forkJoin } from 'rxjs';
import { CouponService } from '../../services/coupon.service';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-cart',
  standalone: true,
  imports: [CommonModule, MaterialModule, ReactiveFormsModule, RouterLink ],
  templateUrl: './cart.component.html',
  styleUrl: './cart.component.scss'
})
export class CartComponent {

  cartItems: any[] = [];
  orderSummary: any ;


  couponForm!: FormGroup;

  constructor(private customerService: CustomerService,
              private snackBar: MatSnackBar,
              private formBuilder: FormBuilder,
              public dialog: MatDialog ,
              private couponService: CouponService 
  ){}

  ngOnInit(){
    this.couponForm = this.formBuilder.group({
      code: [null, [Validators.required]]
    })
    this.getCartAndOrderSummary();
  }

  applyCoupon(){
    const code = this.couponForm.get('code').value;
    this.couponService.setCouponCode(code);
    this.customerService.applyCoupon(this.couponForm.get(['code']).value).subscribe(
      (res) => {    
             this.snackBar.open("Coupon Applied Successfully", "Close", {duration: 5000});
              //updating the orderSummary after applying the coupon          
               this.orderSummary = res;                  
               },       
               (error)=> {        
                   //Extracting whatever error from the backend response
                   const errorMessage = error.error?.message || error.message || 'An unexpected error occured';
                   this.snackBar.open(errorMessage, "Close", {duration: 5000});
                   this.couponService.clearCouponCode();

                 })
    this.couponForm.reset(); 
  }

  getCartAndOrderSummary(){
    this.cartItems = [];
    const cartItemsRequest = this.customerService.getCartByUserId();
    const orderSummaryRequest = this.customerService.getOrderSummary();

    // Use forkJoin to make both HTTP requests in parallel
    //to call two end points from back end in parallel
    forkJoin([cartItemsRequest, orderSummaryRequest]).subscribe(
      ([cartItems, orderSummary]) => {
        this.cartItems = cartItems;
        this.orderSummary = orderSummary;
        this.cartItems.forEach(element => {
          element.processImg = 'data:imge/jpeg;base64, ' + element.returnedImg;
          
        });

      }
    )
  }

  clearCart() {
    this.customerService.clearCartByUserId().subscribe(
      (res) => {
        this.snackBar.open("Cart Is Empty", "Close", {duration: 5000});
        this.getCartAndOrderSummary();
      }
    )
  }

  deleteCartItem(itemId: number) {
    this.customerService.deleteCartItemById(itemId).subscribe(
      (res) => {
        this.snackBar.open("Cart Item Deleted Successfully", "Close", {duration: 5000});
        this.getCartAndOrderSummary();

      }, 
      (error) => {
          this.snackBar.open("Error deleting the Item", "Close", {duration: 5000});
      }
    )
  }

  increaseProductQuantity(productId: any){
     this.customerService.increaseProductQuantity(productId).subscribe(
      (res)=> {
        this.snackBar.open("Product Qunatity increased", "Close", {duration: 5000});
        this.getCartAndOrderSummary();
      }
     )
  }
  decreaseProductQuantity(productId: any) {
     this.customerService.decreaseProductQuantity(productId).subscribe(
      (res) => {
        this.snackBar.open("Product Qunatity decreased", "Close", {duration: 5000});
        this.getCartAndOrderSummary();
      }
     )
  }

  placeOrder(){
    this.dialog.open(PlaceOrderComponent);
  }



}

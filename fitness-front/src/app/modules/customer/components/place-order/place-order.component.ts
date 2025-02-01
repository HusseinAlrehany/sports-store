import { Component } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { CustomerService } from '../../services/customer.service';
import { Router } from '@angular/router';
import { MatDialog } from '@angular/material/dialog';
import { MaterialModule } from '../../../../Material.Module';
import { CommonModule } from '@angular/common';
import { CouponService } from '../../services/coupon.service';

@Component({
  selector: 'app-place-order',
  standalone: true,
  imports: [MaterialModule, ReactiveFormsModule, CommonModule],
  templateUrl: './place-order.component.html',
  styleUrl: './place-order.component.scss'
})
export class PlaceOrderComponent {

  orderForm : FormGroup;

  couponCode: string | null = null;

  constructor(
    private formBuilder: FormBuilder,
    private snackBar: MatSnackBar,
    private customerService: CustomerService,
    private router: Router,
    public dialog: MatDialog,
    private couponService: CouponService

  ){}

  ngOnInit(){
    this.orderForm = this.formBuilder.group({
      address: [null, [Validators.required]],
      orderDescription: [null], 
    })
    
  }

  placeOrder(){
  // Retrieve the coupon code from the CouponService which get the value from the coupon form
  const couponCode = this.couponService.getCouponCode();
  // Create a new object to send to the backend, including the coupon code
  //using the spread operator ... to unpack the properties from the object(orderDTO)
  //and copy them into new object orderPayload and adds couponCode property to the orderPayload
  const orderPayload = { 
    ...this.orderForm.value,
    couponCode: couponCode // Add the coupon code to the payload
  };

  //removing the coupon code from the payload if it is invalid(null or empty)
  if(!couponCode){
    delete orderPayload.couponCode;
  }

    this.customerService.placeOrder(orderPayload).subscribe(
      (res)=> {
        if(res.id != null){
          this.snackBar.open("Order Placed Successfully", "Close", {duration:5000})
          this.closeForm();
          this.router.navigateByUrl("/customer/my_orders");
        }   
      },
      (error)=> {
        const errorMessage = error.error?.message || error.message || 'An unexpected error occured';
        this.snackBar.open(errorMessage, "Close", {duration: 5000})
        this.closeForm(); 
      }
    )
  }

  closeForm(){
    //clear the coupon code from the coupon service when the form is closed
    this.couponService.clearCouponCode();
    this.dialog.closeAll();
  }

}

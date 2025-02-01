import { Component } from '@angular/core';
import { MaterialModule } from '../../../../Material.Module';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { AdminService } from '../../services/admin.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';

@Component({
  selector: 'app-add-coupon',
  standalone: true,
  imports: [MaterialModule, CommonModule, ReactiveFormsModule],
  templateUrl: './add-coupon.component.html',
  styleUrl: './add-coupon.component.scss'
})
export class AddCouponComponent {

  couponForm: FormGroup;

  constructor(private adminService: AdminService,
              private formBuilder: FormBuilder,
              private matSnackBar:MatSnackBar,
              private router: Router
  ){}

  ngOnInit(){
    this.couponForm = this.formBuilder.group({
      name: [null, [Validators.required]],
      code: [null, [Validators.required]],
      discount: [null, [Validators.required]],
      expirationDate: [null, [Validators.required]]
    })
  }

  addCoupon() {
    if(this.couponForm.valid){
      this.adminService.createCoupon(this.couponForm.value).subscribe(
        (res) => {
          if(res.id!= null){
            this.matSnackBar.open("Coupon Added Successfully!", "Close", {duration: 5000});
            this.router.navigateByUrl('/admin/dashboard');

          }
          else {
            this.matSnackBar.open(res.message, 'Close', {duration: 5000, panelClass: 'error-snackbar'});
          }
        }
      )
    }
    else {
      this.couponForm.markAllAsTouched();
    }
  }

}

import { Component } from '@angular/core';
import { AdminService } from '../../services/admin.service';
import { MaterialModule } from '../../../../Material.Module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-coupons',
  standalone: true,
  imports: [MaterialModule, ReactiveFormsModule, CommonModule],
  templateUrl: './coupons.component.html',
  styleUrl: './coupons.component.scss'
})
export class CouponsComponent {

  coupons: [] = [];

  constructor(private adminService: AdminService){}

  ngOnInit(){
    this.getAllCoupons();
  }

  getAllCoupons(){
    this.adminService.getAllCoupons().subscribe(
      (res)=>{
        this.coupons = res;
      }
    )
  }

}
 

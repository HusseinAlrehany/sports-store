import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { AdminService } from '../../services/admin.service';
import { MaterialModule } from '../../../../Material.Module';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-orders',
  standalone: true,
  imports: [CommonModule, MaterialModule],
  templateUrl: './orders.component.html',
  styleUrl: './orders.component.scss'
})
export class OrdersComponent {

  orders: any;

  constructor(private adminService: AdminService, private snackBar: MatSnackBar){}

  ngOnInit(){
    this.getAllOrders();
  }
  getAllOrders() {
    this.adminService.getAllOrders().subscribe(
      (res)=> {
        console.log("Orders are = > " , res);
        this.orders = res;
      }
    )
  }
  changeOrderStatus(orderId: number, status: string){
      this.adminService.changeOrderStatus(orderId, status).subscribe(
        (res)=> {
          console.log("Response" + res);
          if(res.id != null){
            this.snackBar.open("Order status changed successfully", "Close", { duration: 5000});
            this.getAllOrders();
          }
          else {
            this.snackBar.open("Somthing went wrong", "Close", { duration: 5000});

          }
        }
      )
  }
}

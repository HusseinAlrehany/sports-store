import { Component } from '@angular/core';
import { CustomerService } from '../../services/customer.service';
import { MaterialModule } from '../../../../Material.Module';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-my-orders',
  standalone: true,
  imports: [MaterialModule, CommonModule, RouterLink],
  templateUrl: './my-orders.component.html',
  styleUrl: './my-orders.component.scss'
})
export class MyOrdersComponent {

  myOrders: any;

  constructor(private customerService: CustomerService){}

  ngOnInit(){
    this.getMyOrders();
  }
  getMyOrders() {
    this.customerService.getOrdersByUserId().subscribe(
      res => {
        this.myOrders = res;
      }
    )
  }
}

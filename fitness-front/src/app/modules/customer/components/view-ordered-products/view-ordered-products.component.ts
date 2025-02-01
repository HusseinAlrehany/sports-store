import { Component } from '@angular/core';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { CustomerService } from '../../services/customer.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-view-ordered-products',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './view-ordered-products.component.html',
  styleUrl: './view-ordered-products.component.scss'
})
export class ViewOrderedProductsComponent {

  orderId = this.activatedRoute.snapshot.params['orderId'];
  orderedProductsDetailsList = [];
  totalAmount: any;

constructor(private activatedRoute: ActivatedRoute,
            private customerService:CustomerService
){}

ngOnInit(){
  this.getOrderedProductsDetailsByOrderId();
}

getOrderedProductsDetailsByOrderId(){
  this.customerService.getOrderedProducts(this.orderId).subscribe(res=>{
    res.productDTOList.forEach(element => {
       element.processedImg = 'data:image/jpeg;base64,' + element.byteImg;
       this.orderedProductsDetailsList.push(element);
    });

    this.totalAmount = res.orderAmount;

  })
}

}

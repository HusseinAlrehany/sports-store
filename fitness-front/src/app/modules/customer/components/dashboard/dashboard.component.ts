import { Component, OnInit } from '@angular/core';
import { MaterialModule } from '../../../../Material.Module';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { CustomerService } from '../../services/customer.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [MaterialModule, ReactiveFormsModule, CommonModule, RouterLink],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.scss'
})
export class DashboardComponent implements OnInit{
listOfProducts: any = [];

searchForm: FormGroup;

  constructor(private customerService: CustomerService,
              private snackBar: MatSnackBar,
              private formBuilder: FormBuilder
  ){

   this.searchForm = this.formBuilder.group({
    title: ['', [Validators.required]],
   })

  }
  ngOnInit(): void {
    this.getAllProducts();
  }
  getAllProducts() {
    this.listOfProducts = [];
    this.customerService.getAllProducts().subscribe(
      (res)=> {
        console.log(res);
        res.forEach(element=> {
          element.processImg = 'data:imge/jpeg;base64, ' + element.byteImg;
          this.listOfProducts.push(element);
        });
      }
    )
  }

  searchProductByName() {
    this.listOfProducts = [];
    const title = this.searchForm.get('title').value;
    this.customerService.getAllProductsByName(title).subscribe(
      (res)=> {
        res.forEach(element => {
          element.processImg = 'data:imge/jpeg;base64, ' + element.byteImg;
          this.listOfProducts.push(element);
        })
      }
    )
  }

  addToCart(id: number){
      this.customerService.addToCart(id).subscribe(
        (response) => {
            this.snackBar.open("Product added to the cart successfully", "Close", {duration: 5000})
        })
  }

}

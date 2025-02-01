import { Component, OnInit } from '@angular/core';
import { AdminService } from '../../services/admin.service';
import { Router, RouterLink } from '@angular/router';
import { MaterialModule } from '../../../../Material.Module';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { MatSnackBar } from '@angular/material/snack-bar';

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

  constructor(private adminService: AdminService,
              private router: Router,
              private formBuilder: FormBuilder,
              private snackBar: MatSnackBar
  ){

     this.searchForm = this.formBuilder.group({
      title: ['', [Validators.required]],
     })
  }

  ngOnInit(): void {
    this.getAllProducts();
  }
  getAllProducts() {
    //this.listOfProducts = []; setting the list of product to empty array
    //to not load the previous deleted products for the user
    //and this is obviously noticed when deleteting a product
    this.listOfProducts = [];
    this.adminService.getAllProducts().subscribe(
      (res)=> {
        console.log(res);
        //looping through every element to conactenate the string 'data:imge/jpeg;base64, '
        res.forEach(element => {
            element.processImg = 'data:imge/jpeg;base64, ' + element.byteImg;
            this.listOfProducts.push(element);
        });
      }
    )
  }

  searchProductByName() {
    this.listOfProducts = [];
     const title = this.searchForm.get('title').value;
     this.adminService.getAllProductsByName(title).subscribe(
      (res)=> {
         console.log(res);
         res.forEach(element => {
          element.processImg = 'data:imge/jpeg;base64, ' + element.byteImg;
          this.listOfProducts.push(element);
      });
      }
     )
  }

  deleteProduct(id: any) {
    this.adminService.deleteProductById(id).subscribe(
      (res)=> {
        this.snackBar.open("Product Deleted Success!", "Close", {duration: 5000});
        this.getAllProducts();
      },
      (error)=> {
        this.snackBar.open("Somthing went wrong!", "Close", {duration: 5000, panelClass:"error-snackbar"});

      }
    )
      
  }

}

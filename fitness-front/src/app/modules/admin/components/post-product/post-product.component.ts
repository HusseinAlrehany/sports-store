import { Component } from '@angular/core';
import { MaterialModule } from '../../../../Material.Module';
import { FormBuilder, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { MatSnackBar } from '@angular/material/snack-bar';
import { AdminService } from '../../services/admin.service';
import { OnInit } from '@angular/core';
import { Validators } from '@angular/forms';

@Component({
  selector: 'app-post-product',
  standalone: true,
  imports: [MaterialModule, ReactiveFormsModule, CommonModule],
  templateUrl: './post-product.component.html',
  styleUrl: './post-product.component.scss'
})
export class PostProductComponent implements OnInit{

  productForm: FormGroup;
  listOfCategories: any = [];
  selectedFile: File | null;
  imagePreview: string | ArrayBuffer | null;

  constructor(private formBuilder: FormBuilder,
              private router: Router,
              private snackBar: MatSnackBar,
              private adminService: AdminService
  ){}
  
  onFileSelected(event: any){
     this.selectedFile = event.target.files[0];
     this.previewImage();
  }
  previewImage() {
    
    const reader = new FileReader();
    reader.onload = () => {
      //update imagePreview 
      this.imagePreview = reader.result;
    }

    reader.readAsDataURL(this.selectedFile);
  }

  ngOnInit(): void {
    this.productForm = this.formBuilder.group({
      categoryId: [null, [Validators.required]],
      name: [null, [Validators.required]],
      price: [null, [Validators.required]],
      description: [null, [Validators.required]]
    });


    this.getAllCategories();
  }
  getAllCategories() {
    this.adminService.getAllCategories().subscribe(
      (response)=> {
        this.listOfCategories = response;
      }
    )
  }

  addProduct() {
    if(this.productForm.valid){
      //using form data which is a build in object in typescript
      //used for sending data to the server especially uploading files
      const formData: FormData = new FormData();
      formData.append('img', this.selectedFile);
      formData.append('categoryId', this.productForm.get('categoryId').value);
      formData.append('name', this.productForm.get('name').value);
      formData.append('price', this.productForm.get('price').value);
      formData.append('description', this.productForm.get('description').value);

      this.adminService.addProduct(formData).subscribe(
        (response)=> {
             //check of the product created and take id
             if(response.id != null){
                this.snackBar.open("Product Created Successfully", "Close", {duration: 5000});
                this.router.navigateByUrl('/admin/dashboard');
             } else {
              //getting the error message from the response from the API
               this.snackBar.open(response.message, 'ERROR', {duration: 5000});
             }
        }
      )
    }
    else {
      this.productForm.markAllAsTouched();
        //OR
      /*for(const i in this.productForm.controls){
        this.productForm.controls[i].markAsDirty();
        this.productForm.controls[i].updateValueAndValidity();
      }*/
    }

  }







}

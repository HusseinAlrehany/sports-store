import { Component } from '@angular/core';
import { MaterialModule } from '../../../../Material.Module';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { AdminService } from '../../services/admin.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-update-product',
  standalone: true,
  imports: [MaterialModule, CommonModule, ReactiveFormsModule],
  templateUrl: './update-product.component.html',
  styleUrl: './update-product.component.scss'
})
export class UpdateProductComponent {

  productId: number = this.activatedRoute.snapshot.params["productId"];

  productForm: FormGroup;
    listOfCategories: any = [];
    selectedFile: File | null;
    imagePreview: string | ArrayBuffer | null;
    existingImage: string | null = null;
    imgChanged = false;
  
    constructor(private formBuilder: FormBuilder,
                private router: Router,
                private snackBar: MatSnackBar,
                private adminService: AdminService,
                private activatedRoute: ActivatedRoute
    ){}
    
    onFileSelected(event: any){
       //this.selectedFile = event.target.files[0];
       const file = event.target.files[0];
       if(file){
        this.selectedFile = file;
        this.previewImage();
        this.imgChanged = true;
        this.existingImage = null;
       }
       else {
        this.selectedFile = null;
        this.imgChanged = false;
        this.imagePreview = this.existingImage;
       }
       
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
      this.getProductById();
    }
    getAllCategories() {
      this.adminService.getAllCategories().subscribe(
        (response)=> {
          this.listOfCategories = response;
        }
      )
    }

    getProductById(){
      this.adminService.getProductById(this.productId).subscribe(
        //repopulate the product form with values
        res=> {this.productForm.patchValue(res);
               this.existingImage = 'data:image/jpeg;base64,' + res.byteImg;        
        }
      )
    }
  
    updateProduct() {
      if(this.productForm.valid){
        //using form data which is a build in object in typescript
        //used for sending data to the server especially uploading files
        const formData: FormData = new FormData();

        if(this.imgChanged && this.selectedFile){
          formData.append('img', this.selectedFile);
        }
        
        formData.append('categoryId', this.productForm.get('categoryId').value);
        formData.append('name', this.productForm.get('name').value);
        formData.append('price', this.productForm.get('price').value);
        formData.append('description', this.productForm.get('description').value);
  
        this.adminService.updateProduct(this.productId ,formData).subscribe(
          (response)=> {
               //check of the product created and take id
               if(response.id != null){
                  this.snackBar.open("Product Updated Successfully", "Close", {duration: 5000});
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

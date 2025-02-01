import { Component } from '@angular/core';
import { CustomerService } from '../../services/customer.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MaterialModule } from '../../../../Material.Module';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { StorageService } from '../../../../services/local-storage/storage.service';
import { ReviewService } from '../../services/review.service';

@Component({
  selector: 'app-review-ordered-products',
  standalone: true,
  imports: [MaterialModule, CommonModule, ReactiveFormsModule],
  templateUrl: './review-ordered-products.component.html',
  styleUrl: './review-ordered-products.component.scss'
})
export class ReviewOrderedProductsComponent {

productId: number = this.activatedRoute.snapshot.params['productId'];
reviewForm: FormGroup;
selectedFile: File | null;
imagePreview: string | ArrayBuffer | null;

  constructor(private customerService: CustomerService,
              private snackBar: MatSnackBar,
              private formBuilder: FormBuilder,
              private router: Router,
              private activatedRoute: ActivatedRoute, 
              private reviewService: ReviewService
             
  ){}

  ngOnInit(){
    this.reviewForm = this.formBuilder.group({
      rating: [null, [Validators.required]],
      description: [null, [Validators.required]]
    })
  }

  onFileSelected(event: any){
    this.selectedFile = event.target.files[0];
    this.previewImg();
  }
  previewImg() {
    const reader = new FileReader();
    reader.onload = () => {
      this.imagePreview = reader.result;
    }

    reader.readAsDataURL(this.selectedFile);
  }

  submitForm(){
    const formData = new FormData();
    formData.append('img', this.selectedFile);
    formData.append('productId', this.productId.toString());
    formData.append('userId', StorageService.getUserId().toString()); 
    formData.append('rating', this.reviewForm.get('rating').value);
    formData.append('description', this.reviewForm.get('description').value);

    this.customerService.createReview(formData).subscribe(res => {
      if(res != null) {
        this.snackBar.open("Review Created Successsfully!", "Close", {duration: 5000});
        //notify the ProductDetailsComponenet that a new review is added
        this.reviewService.notifyReviewAdded();
        this.router.navigateByUrl(`/customer/product_details/${this.productId}`);
      } else{
        this.snackBar.open("Something went wrong!", "ERROR", {duration: 5000});
      }

      })
      }
  }
    
    
  



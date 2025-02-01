import { Component, ElementRef, ViewChild } from '@angular/core';
import { CustomerService } from '../../services/customer.service';
import { ActivatedRoute, Router } from '@angular/router';
import { MaterialModule } from '../../../../Material.Module';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatDialog } from '@angular/material/dialog';
import { ImageModelComponent } from '../image-model/image-model.component';
import { CommonModule } from '@angular/common';
import { ReviewService } from '../../services/review.service';
import {MatExpansionModule} from '@angular/material/expansion';

@Component({
  selector: 'app-product-details',
  standalone: true,
  imports: [MaterialModule, CommonModule, MatExpansionModule],
  templateUrl: './product-details.component.html',
  styleUrl: './product-details.component.scss'
})
export class ProductDetailsComponent {

  productId: number = this.activatedRoute.snapshot.params['productId'];

  @ViewChild('reviewsSection') reviewsSection!: ElementRef;

  productDetails: any = {};

  processImg: string = '';

  userName: string = '';

  rating: number = 0;

  description: string = '';

  reviews: any[] = [];

  faqs: any [] = [];
  question: string = '';
  answer: string = '';

  averageRating: number = 0; // Example average rating
  totalRatings: number = 0; // Example total ratings

  constructor(private customerService: CustomerService,
              private activatedRoute: ActivatedRoute,
              private snackBar: MatSnackBar,
              private dialoge: MatDialog,
              private router: Router,
              private reviewService: ReviewService
  ){}


  ngOnInit(){
    this.getProductDetails();

    //listen for any updates in review component and reload product details
    this.reviewService.reviewAdded$.subscribe(
      (addedReview)=> {
        if(addedReview) {
          this.getProductDetails();
        }
      }
    )
    
  }
  getProductDetails() {
    this.customerService.getProductDetails(this.productId).subscribe(
      (res)=> {
        this.processImg = 'data:image/jpeg;base64, ' + res.byteImg;
        this.productDetails = res;       
       
        this.totalRatings = res.totalRating || 0;
        this.averageRating = res.averageRating || 0;

        this.reviews = res.reviews || [];
        this.faqs = res.faqdtos || [];

        this.reviews.forEach(element =>{
          this.rating = element.rating;
          this.description = element.description;
          this.userName = element.userName;

          console.log("Rating " + this.rating);
          console.log("description " + this.description);
          console.log("userName " + this.userName);
          
        });

        //Frequently Asked Question
        this.faqs.forEach(element => {
          this.question = element.question;
          this.answer = element.answer;

          console.log("question " + this.question);
          console.log("answer " + this.answer);
        })
       
      },
      (error)=> {
         const errorMessage = error.error?.message || error.message || 'An unexpected error occured';
         this.snackBar.open(errorMessage, "Close", {duration: 3000})
      }
    )
  }

  openImageModel(){
      this.dialoge.open(ImageModelComponent, {
        data: this.processImg,
        panelClass: 'image-modal-container'
      });
  }

  addToCart(){
    this.customerService.addToCart(this.productId).subscribe(
      (res)=> {
        this.snackBar.open("Product Added To The Cart Successfully!", "Close", {duration: 3000});
        this.router.navigateByUrl("/customer/cart")
      },
      (error)=> {
         const errorMessage = error.error?.message || error.message || 'An unexpected error occured';
         this.snackBar.open(errorMessage, "Close", {duration: 3000});
      }
    )
  }

  scrollToReviews(){
    if (this.reviewsSection && this.reviewsSection.nativeElement) {
      this.reviewsSection.nativeElement.scrollIntoView({ behavior: 'smooth', block: 'start' });
  }

} 
}

import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ReviewService {

  private addedReviewSource = new BehaviorSubject<boolean>(false);

  reviewAdded$ = this.addedReviewSource.asObservable();

  notifyReviewAdded(){
    this.addedReviewSource.next(true);
  }

}

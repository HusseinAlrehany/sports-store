package com.coding.fitness.controller.customer;

import com.coding.fitness.dtos.OrderedProductsDTO;
import com.coding.fitness.dtos.ReviewDTO;
import com.coding.fitness.services.customer.review.ProductReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customer")
@RequiredArgsConstructor
public class ReviewController {

    private final ProductReviewService productReviewService;

    @GetMapping("/ordered-products/{orderId}")
    public ResponseEntity<OrderedProductsDTO> reviewOrderedProduct(@PathVariable Long orderId){
        return ResponseEntity.ok(productReviewService.reviewOrderedProduct(orderId));
    }

    @PostMapping("/review")
    public ResponseEntity<?> createReview(@ModelAttribute ReviewDTO reviewDTO){
        ReviewDTO reviewDTO1 = productReviewService.createReview(reviewDTO);

        return reviewDTO1 == null ?
                ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Something went wrong!") :
                ResponseEntity.status(HttpStatus.CREATED)
                        .body(reviewDTO1);
    }
}

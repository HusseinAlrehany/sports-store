package com.coding.fitness.controller.admin;

import com.coding.fitness.services.admin.review.AdminReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminReviewController {

    private final AdminReviewService adminReviewService;

    @DeleteMapping("/review/{reviewId}")
    public ResponseEntity<Void> deleteReviewById(@PathVariable Long reviewId){
          adminReviewService.deleteReviewById(reviewId);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
}

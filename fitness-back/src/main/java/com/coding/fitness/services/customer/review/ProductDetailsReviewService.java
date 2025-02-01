package com.coding.fitness.services.customer.review;

import com.coding.fitness.dtos.OrderedProductsDTO;
import com.coding.fitness.dtos.ReviewDTO;

public interface ProductDetailsReviewService {

    public OrderedProductsDTO getProductDetailsAndReviewByOrderId(Long orderId);

    ReviewDTO createReview(ReviewDTO reviewDTO);
}

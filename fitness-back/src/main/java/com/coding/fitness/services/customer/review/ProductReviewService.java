package com.coding.fitness.services.customer.review;

import com.coding.fitness.dtos.OrderedProductsDTO;
import com.coding.fitness.dtos.ReviewDTO;

public interface ProductReviewService {

    public OrderedProductsDTO reviewOrderedProduct(Long orderId);

    ReviewDTO createReview(ReviewDTO reviewDTO);
}

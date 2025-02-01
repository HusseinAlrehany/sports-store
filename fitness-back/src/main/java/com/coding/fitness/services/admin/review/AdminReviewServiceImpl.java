package com.coding.fitness.services.admin.review;

import com.coding.fitness.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminReviewServiceImpl implements AdminReviewService{

    private final ReviewRepository reviewRepository;
    @Override
    public void deleteReviewById(Long reviewId) {
        reviewRepository.deleteById(reviewId);
    }
}

package com.coding.fitness.mapper;

import com.coding.fitness.dtos.ReviewDTO;
import com.coding.fitness.entity.Review;
import com.coding.fitness.entity.User;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
public class ReviewMapper {

    public ReviewDTO toDTO(Review review){
        ReviewDTO reviewDTO = new ReviewDTO();
        reviewDTO.setId(review.getId());
        reviewDTO.setDescription(review.getDescription());
        reviewDTO.setRating(review.getRating());
        reviewDTO.setReturnedImg(review.getImg());
        reviewDTO.setProductId(review.getProduct().getId());
        reviewDTO.setUserId(review.getUser().getId());
        reviewDTO.setUserName(review.getUser().getName());

        return reviewDTO;
    }

    public Review toEntity(ReviewDTO reviewDTO){
        Review review = new Review();
        review.setImg(review.getImg());
        review.setRating(reviewDTO.getRating());
        review.setDescription(reviewDTO.getDescription());

        User user = new User();
        user.setId(reviewDTO.getUserId());
        user.setName(reviewDTO.getUserName());
        review.setUser(user);

        return review;
    }
}

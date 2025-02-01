package com.coding.fitness.services.customer.review;

import com.coding.fitness.dtos.ProductDTO;
import com.coding.fitness.dtos.OrderedProductsDTO;
import com.coding.fitness.dtos.ReviewDTO;
import com.coding.fitness.entity.*;
import com.coding.fitness.exceptions.ProcessingImgException;
import com.coding.fitness.exceptions.ValidationException;
import com.coding.fitness.mapper.Mapper;
import com.coding.fitness.repository.OrderRepository;
import com.coding.fitness.repository.ProductRepository;
import com.coding.fitness.repository.ReviewRepository;
import com.coding.fitness.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductDetailsReviewServiceImpl implements ProductDetailsReviewService {

    private final OrderRepository orderRepository;

    private final ProductRepository productRepository;

    private final UserRepository userRepository;

    private final ReviewRepository reviewRepository;

    private final Mapper mapper;


    public OrderedProductsDTO getProductDetailsAndReviewByOrderId(Long orderId){
        Optional.ofNullable(orderId)
                .filter(id-> id > 0)
                .orElseThrow(()-> new ValidationException("Invalid OrderID"));
        Order order = orderRepository.findById(orderId)
                .orElseThrow(()-> new ValidationException("No Order Found"));

        OrderedProductsDTO orderedProductsDTO = new OrderedProductsDTO();
        orderedProductsDTO.setOrderAmount(order.getTotalAmount());

        List<ProductDTO> productDTOList = new ArrayList<>();
        for(CartItems cartItems: order.getCartItems()){
            ProductDTO productDTO = new ProductDTO();
            productDTO.setId(cartItems.getProduct().getId());
            productDTO.setPrice(cartItems.getPrice());
            productDTO.setName(cartItems.getProduct().getName());
            productDTO.setQuantity(cartItems.getQuantity());
            productDTO.setByteImg(cartItems.getProduct().getImg());

            productDTOList.add(productDTO);
        }
         orderedProductsDTO.setProductDTOList(productDTOList);

          return orderedProductsDTO;
    }

    public ReviewDTO createReview(ReviewDTO reviewDTO) {
        Product product = productRepository.findById(reviewDTO.getProductId())
                .orElseThrow(()-> new ValidationException("No Product Found"));
        User user = userRepository.findById(reviewDTO.getUserId())
                .orElseThrow(()-> new ValidationException("No User Found"));

        double totalRating = product.getReviews().size() + 1;
        double averageRating = calculateAverageRating(product.getReviews());

        Review review = new Review();
        review.setRating(reviewDTO.getRating());
        review.setProduct(product);
        review.setUser(user);
        review.setDescription(reviewDTO.getDescription());
        product.setTotalRating(totalRating);
        product.setAverageRating(averageRating);

        try{
            review.setImg(reviewDTO.getImg().getBytes());
        } catch(IOException ex){
            throw new ProcessingImgException("Error Processing Img");
        }

        //save the product with the updated totalRating and averageRating
        productRepository.save(product);

        Review savedReview = reviewRepository.save(review);
       return mapper.getReviewDTO(savedReview);
    }

    private Double calculateAverageRating(List<Review> reviews) {

           if(reviews.isEmpty()){
               return 0.0;
           }
           double total = reviews.stream().mapToDouble(Review::getRating).sum();
           return total/ reviews.size();
    }


}

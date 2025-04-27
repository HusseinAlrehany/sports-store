package com.coding.fitness;

import com.coding.fitness.dtos.FAQDTO;
import com.coding.fitness.dtos.ProductDTO;
import com.coding.fitness.dtos.ReviewDTO;
import com.coding.fitness.entity.FAQ;
import com.coding.fitness.entity.Product;
import com.coding.fitness.entity.Review;
import com.coding.fitness.mapper.FAQMapper;
import com.coding.fitness.mapper.ProductMapper;
import com.coding.fitness.mapper.ReviewMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testing Product Mapper")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ProductMapperTest {


    //this mapper has dependencies inside it (ReviewMapper)
    ProductMapper productMapper ;

    @BeforeAll
    void setUp(){
        ReviewMapper reviewMapper = new ReviewMapper();
        FAQMapper faqMapper = new FAQMapper();
        productMapper = new ProductMapper(reviewMapper, faqMapper);
    }



    @Test
    @DisplayName("should map product DTO to product entity")
    public void toProductEntity(){

        FAQDTO faqdto = new FAQDTO(1L, "what is that","i don't know", 1L);

        ReviewDTO reviewDTO = new ReviewDTO(1L, 5L, "Nice", null,
                "test".getBytes(), 1L, 1L, "hussein");


        ProductDTO dto = new ProductDTO(
                1L,
                "Micro",
                20L,
                "microprocessor",
                "test".getBytes(),
                11L,
                "treadmeal",
                null,
                10L,
                12.0,
                4.5,
                List.of(reviewDTO),
                List.of(faqdto));

        Product product = productMapper.toEntity(dto);

        assertNotNull(product);
        assertAll(
                ()-> assertEquals(dto.getId(), product.getId()),
                ()-> assertEquals(dto.getName(),product.getName()),
                ()->assertEquals(dto.getPrice(),product.getPrice()),
                ()->assertEquals(dto.getDescription(),product.getDescription()),
                ()->assertEquals(dto.getAverageRating(),product.getAverageRating()),
                ()->assertEquals(dto.getTotalRating(),product.getTotalRating())

        );


        //validate product's reviews
        assertNotNull(product.getReviews());
       assertEquals(1, product.getReviews().size());
        Review mappedReview = product.getReviews().get(0);
        assertEquals(reviewDTO.getProductId(), mappedReview.getProduct().getId());
        assertEquals(reviewDTO.getRating(), mappedReview.getRating());
        assertEquals(reviewDTO.getDescription(), mappedReview.getDescription());
        assertEquals(reviewDTO.getUserName(), mappedReview.getUser().getName());

        //validate product's faqs
        assertNotNull(product.getFaqs());
       assertEquals(1, product.getFaqs().size());
        FAQ mappedFAQ = product.getFaqs().get(0);
        assertEquals(faqdto.getProductId(), mappedFAQ.getProduct().getId());
        assertEquals(faqdto.getQuestion(), mappedFAQ.getQuestion());
        assertEquals(faqdto.getAnswer(), mappedFAQ.getAnswer());


    }
}

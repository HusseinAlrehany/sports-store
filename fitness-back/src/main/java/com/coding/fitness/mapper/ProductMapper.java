package com.coding.fitness.mapper;

import com.coding.fitness.dtos.FAQDTO;
import com.coding.fitness.dtos.ProductDTO;
import com.coding.fitness.dtos.ReviewDTO;
import com.coding.fitness.entity.FAQ;
import com.coding.fitness.entity.Product;
import com.coding.fitness.entity.Review;
import com.coding.fitness.exceptions.ProcessingImgException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ProductMapper {

    private final ReviewMapper reviewMapper;

    private final FAQMapper faqMapper;

    //convert the Product to ProductDTO
    public ProductDTO toDTO(Product product){

        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(product.getId());
        productDTO.setName(product.getName());
        productDTO.setDescription(product.getDescription());
        productDTO.setPrice(product.getPrice());
        productDTO.setCategoryId(product.getCategory().getId());
        productDTO.setByteImg(product.getImg());
        productDTO.setCategoryName(product.getCategory().getName());
        productDTO.setAverageRating(product.getAverageRating());
        productDTO.setTotalRating(product.getTotalRating());

        if(product.getReviews() != null){
            List<ReviewDTO> reviewDTOS = product.getReviews().stream()
                    .map(reviewMapper::toDTO)
                    .toList();
            productDTO.setReviews(reviewDTOS);
        }

        if(product.getFaqs() != null){
            List<FAQDTO> faqdtos = product.getFaqs().stream()
                            .map(faqMapper::toDTO)
                                    .toList();
            productDTO.setFaqdtos(faqdtos);
        }

        return productDTO;
    }

    public Product toEntity(ProductDTO productDTO){

        Product product = new Product();
        product.setId(productDTO.getId());
        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());
        product.setPrice(productDTO.getPrice());
        product.setTotalRating(productDTO.getTotalRating());
        product.setAverageRating(productDTO.getAverageRating());
        if(productDTO.getImg() != null){
            product.setImg(convertMultipartToByte(productDTO.getImg()));
        }
        if(productDTO.getReviews() != null){
            List<Review> reviews = productDTO.getReviews().stream()
                    .map(reviewMapper::toEntity)
                    .toList();
            reviews.forEach(re-> re.setProduct(product));
            product.setReviews(reviews);
        }

        if(productDTO.getFaqdtos() != null){
            List<FAQ> faqs = productDTO.getFaqdtos().stream()
                    .map(faqMapper::toEntity)
                    .toList();
            faqs.forEach(f-> f.setProduct(product));
            product.setFaqs(faqs);
        }


        return product;
    }

    public List<ProductDTO> toDTOList(List<Product> products){

        return products !=  null
                ? products.stream().map(this::toDTO).collect(Collectors.toList())
                :
                new ArrayList<>();
    }


    //update product
    public void updateProduct(Product product, ProductDTO productDTO){
        product.setName(productDTO.getName());
        product.setPrice(productDTO.getPrice());
        product.setDescription(productDTO.getDescription());
        if(productDTO.getImg() != null){
            product.setImg(convertMultipartToByte(productDTO.getImg()));
        }

    }


    public byte[] convertMultipartToByte(MultipartFile img){
        try {
            return img.getBytes();
        }catch(IOException ex){
            throw new ProcessingImgException("Error processing image");
        }
    }




}

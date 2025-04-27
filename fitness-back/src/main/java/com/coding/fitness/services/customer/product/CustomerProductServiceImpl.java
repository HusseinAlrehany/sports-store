package com.coding.fitness.services.customer.product;

import com.coding.fitness.dtos.ProductDTO;
import com.coding.fitness.entity.Product;
import com.coding.fitness.exceptions.ValidationException;
import com.coding.fitness.mapper.FAQMapper;
import com.coding.fitness.mapper.ProductMapper;
import com.coding.fitness.mapper.ReviewMapper;
import com.coding.fitness.repository.FAQRepository;
import com.coding.fitness.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerProductServiceImpl implements CustomerProductService{


    private final ProductRepository productRepository;

    private final FAQRepository faqRepository;

    private final ProductMapper productMapper;

    private final ReviewMapper reviewMapper;

    private final FAQMapper faqMapper;


    @Override
    public List<ProductDTO> findAll() {

        List<Product> products = productRepository.findAll();
        if(products.isEmpty()){
           throw new ValidationException("No Products Found");
        }
        return productMapper.toDTOList(products);


    }

    @Override
    public List<ProductDTO> findAllProductsByName(String name) {
        List<Product> products = productRepository.findAllByNameContaining(name);
        if(products.isEmpty()){
            throw new ValidationException("No Product Found for that name");
        }
        return productMapper.toDTOList(products);

    }

    @Override
    public ProductDTO getProductById(Long productId) {
        if(productId < 0){
            throw new ValidationException("Invalid productId");
        }
        Product product = productRepository.findById(productId)
                .orElseThrow(()-> new ValidationException("No Product Found"));

        //this block is commented since productMapper.toDTO(product) do the same job
        //List <FAQ> faqs = faqRepository.findByProductId(productId);

       /* ProductDTO productDTO = new ProductDTO();
        productDTO.setId(product.getId());
        productDTO.setName(product.getName());
        productDTO.setDescription(product.getDescription());
        productDTO.setPrice(product.getPrice());
        productDTO.setTotalRating(product.getTotalRating());
        productDTO.setAverageRating(product.getAverageRating());
        productDTO.setCategoryId(product.getCategory().getId());
        productDTO.setByteImg(product.getImg());
        productDTO.setCategoryName(product.getCategory().getName());
        productDTO.setReviews(product.getReviews().stream().map(reviewMapper::toDTO).toList());
        productDTO.setFaqdtos(product.getFaqs().stream().map(faqMapper::toDTO).toList());*/

        return productMapper.toDTO(product);
    }

}

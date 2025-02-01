package com.coding.fitness.services.customer;

import com.coding.fitness.dtos.ProductDTO;
import com.coding.fitness.entity.FAQ;
import com.coding.fitness.entity.Product;
import com.coding.fitness.exceptions.ValidationException;
import com.coding.fitness.mapper.Mapper;
import com.coding.fitness.repository.FAQRepository;
import com.coding.fitness.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerProductServiceImpl implements CustomerProductService{


    private final ProductRepository productRepository;

    private final FAQRepository faqRepository;

    private final Mapper mapper;


    @Override
    public List<ProductDTO> findAll() {
        return productRepository.findAll()
                .stream()
                .map(mapper::getProductDTO)
                .collect(Collectors.toList());

    }

    @Override
    public List<ProductDTO> findAllProductsByName(String name) {
        return productRepository.findAllByNameContaining(name)
                .stream()
                .map(mapper::getProductDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ProductDTO getProductById(Long productId) {
        Optional.ofNullable(productId)
                .filter(id-> id > 0)
                .orElseThrow(()-> new ValidationException("Invalid productId"));
        Product product = productRepository.findById(productId)
                .orElseThrow(()-> new ValidationException("No Product Found"));

        List <FAQ> faqs = faqRepository.findByProductId(productId);

        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(product.getId());
        productDTO.setName(product.getName());
        productDTO.setDescription(product.getDescription());
        productDTO.setPrice(product.getPrice());
        productDTO.setTotalRating(product.getTotalRating());
        productDTO.setAverageRating(product.getAverageRating());
        productDTO.setCategoryId(product.getCategory().getId());
        productDTO.setByteImg(product.getImg());
        productDTO.setCategoryName(product.getCategory().getName());
        productDTO.setReviews(product.getReviews().stream().map(mapper::getReviewDTO).toList());
        productDTO.setFaqdtos(product.getFaqs().stream().map(mapper::getFAQDTO).toList());

        return productDTO;
    }

}

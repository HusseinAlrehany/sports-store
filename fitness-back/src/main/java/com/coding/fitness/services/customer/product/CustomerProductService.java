package com.coding.fitness.services.customer.product;

import com.coding.fitness.dtos.ProductDTO;

import java.util.List;

public interface CustomerProductService {


    List<ProductDTO> findAll();

    List<ProductDTO> findAllProductsByName(String name);

    ProductDTO getProductById(Long productId);
}

package com.coding.fitness.dtos;

import lombok.Data;

import java.util.List;

@Data
public class OrderedProductsDTO {
    private List<ProductDTO> productDTOList;
    private Long orderAmount;
}

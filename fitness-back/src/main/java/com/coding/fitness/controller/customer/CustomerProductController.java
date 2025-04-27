package com.coding.fitness.controller.customer;

import com.coding.fitness.dtos.ProductDTO;
import com.coding.fitness.services.customer.product.CustomerProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/customer")
@RequiredArgsConstructor
public class CustomerProductController {

    @Autowired
    private final CustomerProductService customerProductService;


    @GetMapping("/products")
    public ResponseEntity<List<ProductDTO>> findAll(){
        return ResponseEntity.ok(customerProductService.findAll());

    }

    @GetMapping("/search/{name}")
    public ResponseEntity<List<ProductDTO>> getAllProductsByName(@PathVariable String name){
        List<ProductDTO> productDTOS = customerProductService.findAllProductsByName(name);
        return ResponseEntity.ok(productDTOS);
    }

    @GetMapping("/products/{productId}")
    public ResponseEntity<?> getProductById(@PathVariable Long productId){
        ProductDTO productDTO = customerProductService.getProductById(productId);

        return productDTO == null ? ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Something went wrong!")
                : ResponseEntity.ok(productDTO);
    }

}

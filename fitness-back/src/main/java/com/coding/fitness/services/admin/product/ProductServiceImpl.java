package com.coding.fitness.services.admin.product;

import com.coding.fitness.dtos.ProductDTO;
import com.coding.fitness.entity.Category;
import com.coding.fitness.entity.Product;
import com.coding.fitness.exceptions.ValidationException;
import com.coding.fitness.mapper.ProductMapper;
import com.coding.fitness.repository.CategoryRepository;
import com.coding.fitness.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService{

    private final ProductRepository productRepository;

    private final CategoryRepository categoryRepository;

    private final ProductMapper productMapper;
    @Override
    public ProductDTO addProduct(ProductDTO productDTO) {

      Product product = productMapper.toEntity(productDTO);

        Category category = categoryRepository.findById(productDTO.getCategoryId()).orElseThrow(
               ()-> new ValidationException("No Category found"));

        product.setCategory(category);

        Product dbProduct = productRepository.save(product);
        return productMapper.toDTO(dbProduct);
    }

    @Override
    public List<ProductDTO> findAll() {
        List<Product> productList = productRepository.findAll();

        if(productList.isEmpty()){
            throw new ValidationException("No Products Found");
        }
        return  productMapper.toDTOList(productList);

    }

    @Override
    public List<ProductDTO> findAllProductsByName(String name) {
          if(name.isBlank()){
             throw new ValidationException("Invalid product name");
          }

           List<Product> productsList = productRepository.findAllByNameContaining(name);

          if(productsList.isEmpty()){
              throw new ValidationException("OOPS! No Products Found");
          }

        return productMapper.toDTOList(productsList);
    }

    @Override
    public void deleteProduct(Long productId) {
           productRepository.findById(productId)
                           .orElseThrow(()-> new ValidationException("Product Not Found"));
        productRepository.deleteById(productId);
    }

    @Override
    public ProductDTO findProductById(Long productId) {
        if(productId < 0){
            throw new ValidationException("Invalid productId");
        }
       Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ValidationException("No Product Found"));

        return productMapper.toDTO(product);
    }

    @Override
    public ProductDTO updateProduct(Long productId,ProductDTO productDTO) {
        if(productId < 0){
            throw new ValidationException("Invalid Id");
        }
        Product product = productRepository.findById(productId)
                .orElseThrow(()-> new ValidationException("No Product Found"));

         Category category = categoryRepository.findById(productDTO.getCategoryId())
                .orElseThrow(()-> new ValidationException("No category found"));

         product.setCategory(category);

        productMapper.updateProduct(product, productDTO);

        return productMapper.toDTO(productRepository.save(product));
    }


}

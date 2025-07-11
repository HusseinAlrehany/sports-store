package com.coding.fitness.services.admin.category;

import com.coding.fitness.dtos.CategoryDTO;
import com.coding.fitness.entity.Category;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CategoryService {

    CategoryDTO createCategory(CategoryDTO categoryDTO);

    List<CategoryDTO> findAll();
}

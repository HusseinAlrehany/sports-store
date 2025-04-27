package com.coding.fitness.mapper;

import com.coding.fitness.dtos.CategoryDTO;
import com.coding.fitness.entity.Category;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CategoryMapper {


    public CategoryDTO toDTO(Category category){
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setId(category.getId());
        categoryDTO.setName(category.getName());
        categoryDTO.setDescription(category.getDescription());

        return categoryDTO;
    }

    public Category toEntity(CategoryDTO categoryDTO){
        Category category = new Category();
        category.setName(categoryDTO.getName());
        category.setDescription(categoryDTO.getDescription());

        return category;
    }

    public List<CategoryDTO> toDTOList(List<Category> categories){
        return categories != null ? categories.stream()
                .map(this::toDTO)
                .toList() :
                new ArrayList<>();
    }
}

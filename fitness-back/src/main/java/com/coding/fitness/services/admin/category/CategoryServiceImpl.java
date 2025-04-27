package com.coding.fitness.services.admin.category;

import com.coding.fitness.dtos.CategoryDTO;
import com.coding.fitness.entity.Category;
import com.coding.fitness.exceptions.ValidationException;
import com.coding.fitness.mapper.CategoryMapper;
import com.coding.fitness.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    private final CategoryMapper categoryMapper;

    @Override
    public CategoryDTO createCategory(CategoryDTO categoryDTO) {

          Category category = categoryMapper.toEntity(categoryDTO);

        return categoryMapper.toDTO(categoryRepository.save(category));
    }

    @Override
    public List<CategoryDTO> findAll() {
        List<Category> categories = categoryRepository.findAll();
        if(categories.isEmpty()){
            throw new ValidationException("No Categories Found");
        }

        return categoryMapper.toDTOList(categories);
    }
}

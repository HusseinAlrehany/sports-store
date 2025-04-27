package com.coding.fitness.services.admin.faq;

import com.coding.fitness.dtos.FAQDTO;
import com.coding.fitness.entity.FAQ;
import com.coding.fitness.entity.Product;
import com.coding.fitness.exceptions.ValidationException;
import com.coding.fitness.mapper.FAQMapper;
import com.coding.fitness.repository.FAQRepository;
import com.coding.fitness.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FAQServiceImpl implements FAQService{

    private final FAQRepository faqRepository;
    private final ProductRepository productRepository;
    private final FAQMapper faqMapper;

    public FAQDTO createFAQ(Long productId, FAQDTO faqdto){

        if(productId < 0){
            throw new ValidationException("Invalid product id");
        }

        if(faqdto.getQuestion().isBlank() || faqdto.getAnswer().isBlank()){
            throw new ValidationException("Invalid question or answer");
        }
         Product product = productRepository.findById(productId)
                .orElseThrow(()-> new ValidationException("No Product Found"));

        FAQ faq = faqMapper.toEntity(faqdto);

         faq.setProduct(product);

         return faqMapper.toDTO(faqRepository.save(faq));


    }
}

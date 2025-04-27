package com.coding.fitness.mapper;

import com.coding.fitness.dtos.FAQDTO;
import com.coding.fitness.entity.FAQ;
import com.coding.fitness.entity.Product;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class FAQMapper {


    public FAQDTO toDTO(FAQ faq){
        FAQDTO faqdto = new FAQDTO();
        faqdto.setId(faq.getId());
        faqdto.setQuestion(faq.getQuestion());
        faqdto.setAnswer(faq.getAnswer());
        faqdto.setProductId(faq.getProduct().getId());
        return faqdto;
    }

    public FAQ toEntity(FAQDTO faqdto){
        FAQ faq = new FAQ();
        faq.setAnswer(faqdto.getAnswer());
        faq.setQuestion(faqdto.getQuestion());

        return faq;
    }

    public List<FAQDTO> faqdtoList(List<FAQ> faqs){

        return faqs != null ?
                faqs.stream().map(this::toDTO).toList() :
                new ArrayList<>();
    }


}

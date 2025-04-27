package com.coding.fitness.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FAQDTO {

    private Long id;
    private String question;
    private String answer;
    private Long productId;
}

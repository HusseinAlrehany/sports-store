package com.coding.fitness.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewDTO {

    private Long id;
    private Long rating;
    private String description;
    private MultipartFile img;
    private byte[] returnedImg;
    private Long userId;
    private Long productId;
    private String userName;
}

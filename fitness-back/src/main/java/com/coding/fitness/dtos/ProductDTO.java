package com.coding.fitness.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {

    private Long id;

    private String name;
    private Long price;

    private String description;

    private byte[] byteImg;

    //to link the product to the category
    private Long categoryId;
    private String categoryName;

    //to get the image as multipart from the front end
    //then return it as byte array
    private MultipartFile img;

    private Long quantity;

    private Double totalRating;

    private Double averageRating;

    private List<ReviewDTO> reviews = new ArrayList<>();

    private List<FAQDTO> faqdtos = new ArrayList<>();

}

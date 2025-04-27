package com.coding.fitness.dtos;

import com.coding.fitness.enums.UserRole;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UserProfileDTO {

    private Long id;
    private String name;

    private String email;

    private String password;
    private UserRole userRole;
    private MultipartFile image;
    private byte[] byteImg;
    private String updatedToken;
}

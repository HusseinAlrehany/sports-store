package com.coding.fitness.dtos;

import com.coding.fitness.enums.UserRole;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UserDTO {

    private Long id;
    private String name;

    private String email;

    private String password;
    private UserRole userRole;
    private MultipartFile image;
    private byte[] byteImg;
    private String updatedToken;

}

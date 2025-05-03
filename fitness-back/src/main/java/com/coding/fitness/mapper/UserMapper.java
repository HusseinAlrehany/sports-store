package com.coding.fitness.mapper;

import com.coding.fitness.dtos.UserDTO;
import com.coding.fitness.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {


    public UserDTO toDTO(User user){
        if(user == null){
            throw new NullPointerException("User is Null");
        }

        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setName(user.getName());
        userDTO.setEmail(user.getEmail());

        return userDTO;
    }

    public User toEntity(UserDTO userDTO){
        if(userDTO == null){
            throw new NullPointerException("UserDTO is Null");
        }
        User user = new User();
        user.setId(userDTO.getId());
        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());

        return user;
    }

}

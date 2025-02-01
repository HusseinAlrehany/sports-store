package com.coding.fitness.services.customer;

import com.coding.fitness.dtos.UserDTO;

public interface UserProfileService {

    UserDTO updateUserProfile(Long userId, UserDTO userDTO);

    UserDTO getUserById(Long userId);
}

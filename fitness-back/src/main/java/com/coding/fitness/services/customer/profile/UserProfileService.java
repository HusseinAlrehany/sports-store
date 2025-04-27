package com.coding.fitness.services.customer.profile;

import com.coding.fitness.dtos.AuthenticationResponse;
import com.coding.fitness.dtos.UserDTO;
import com.coding.fitness.dtos.UserProfileDTO;

public interface UserProfileService {

    UserProfileDTO updateUserProfile(Long userId, UserProfileDTO userProfileDTO);

    UserProfileDTO getUserById(Long userId);
}

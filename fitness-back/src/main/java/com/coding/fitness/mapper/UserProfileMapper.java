package com.coding.fitness.mapper;

import com.coding.fitness.dtos.UserProfileDTO;
import com.coding.fitness.entity.User;
import com.coding.fitness.tokens.jwtservice.AppUserDetailsService;
import com.coding.fitness.tokens.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserProfileMapper {

    private final JwtUtils jwtUtils;

    private final AppUserDetailsService appUserDetailsService;


    public UserProfileDTO toDTO(User user){
        UserProfileDTO userProfileDTO = new UserProfileDTO();
        userProfileDTO.setId(user.getId());
        userProfileDTO.setUserRole(user.getRole());
        userProfileDTO.setName(user.getName());
        userProfileDTO.setEmail(user.getEmail());
        userProfileDTO.setPassword(user.getPassword());
        userProfileDTO.setByteImg(user.getImg());

        final UserDetails userDetails = appUserDetailsService.loadUserByUsername(
                userProfileDTO.getEmail());
        final String jwtToken = jwtUtils.generateToken(userDetails);

        userProfileDTO.setUpdatedToken(jwtToken);

        return userProfileDTO;
    }

}

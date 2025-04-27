package com.coding.fitness.services.customer.profile;
import com.coding.fitness.dtos.UserProfileDTO;
import com.coding.fitness.entity.User;
import com.coding.fitness.exceptions.ProcessingImgException;
import com.coding.fitness.exceptions.ValidationException;
import com.coding.fitness.mapper.UserProfileMapper;
import com.coding.fitness.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserProfileServiceImpl implements UserProfileService{

    private final UserRepository userRepository;

    private final UserProfileMapper userProfileMapper;

    @Override
    public UserProfileDTO updateUserProfile(Long userId, UserProfileDTO userProfileDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new ValidationException("No User Found!"));

        if(!user.getEmail().equals(userProfileDTO.getEmail())){
            Optional<User> optionalUser = userRepository.findFirstByEmail(userProfileDTO.getEmail());
            if(optionalUser.isPresent()){
                throw new ValidationException("This Email is Already Registered");
            }
            user.setEmail(userProfileDTO.getEmail());
        }

        if(userProfileDTO.getImage() != null){
            try {
                user.setImg(userProfileDTO.getImage().getBytes());
            }
            catch(IOException ex){
                throw  new ProcessingImgException("Error processing img");
            }
        }

        user.setName(userProfileDTO.getName());
        user.setPassword(new BCryptPasswordEncoder().encode(userProfileDTO.getPassword()));


        return userProfileMapper.toDTO(userRepository.save(user));
    }

    @Override
    public UserProfileDTO getUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new ValidationException("No User Found"));

        return userProfileMapper.toDTO(user);
    }
}

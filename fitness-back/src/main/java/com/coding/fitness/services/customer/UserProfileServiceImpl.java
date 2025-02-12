package com.coding.fitness.services.customer;
import com.coding.fitness.dtos.UserDTO;
import com.coding.fitness.entity.User;
import com.coding.fitness.exceptions.ProcessingImgException;
import com.coding.fitness.exceptions.ValidationException;
import com.coding.fitness.mapper.Mapper;
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

    private final Mapper mapper;


    @Override
    public UserDTO updateUserProfile(Long userId, UserDTO userDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ValidationException("No User Found"));

        if(!user.getEmail().equals(userDTO.getEmail())){

            Optional<User> optionalUser = userRepository.findFirstByEmail(userDTO.getEmail());
            if(optionalUser.isPresent()){
                throw new ValidationException("This Email is Already Registered");
            }
            user.setEmail(userDTO.getEmail());
        }

        user.setId(userId);
        user.setName(userDTO.getName());
        user.setPassword(new BCryptPasswordEncoder().encode(userDTO.getPassword()));

         if(userDTO.getImage() != null){

             try {
                 user.setImg(userDTO.getImage().getBytes());
             }
             catch(IOException ex){
                 throw new ProcessingImgException("Error processing Img");
             }

         }


        return mapper.getUserDTO(userRepository.save(user));
    }

    @Override
    public UserDTO getUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new ValidationException("No User Found"));

        return mapper.getUserDTO(user);
    }
}

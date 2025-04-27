package com.coding.fitness.controller.customer;
import com.coding.fitness.dtos.UserProfileDTO;
import com.coding.fitness.services.customer.profile.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customer")
@RequiredArgsConstructor
public class UserProfileController {

  private final UserProfileService userProfileService;

  @PutMapping("/edit-profile/{userId}")
  public ResponseEntity<UserProfileDTO> updateUserProfile (@PathVariable Long userId,
                                                                   @ModelAttribute UserProfileDTO userDTO){

      UserProfileDTO userDTO1 = userProfileService.updateUserProfile(userId, userDTO);
      return userDTO1 != null ? ResponseEntity.ok(userDTO1) :
                                ResponseEntity.badRequest().build();
  }

  @GetMapping("/getUser/{userId}")
  public ResponseEntity<UserProfileDTO> getUserById(@PathVariable Long userId){
      UserProfileDTO userDTO = userProfileService.getUserById(userId);
      return userDTO != null ? ResponseEntity.ok(userDTO) :
                               ResponseEntity.badRequest().build();
  }

}

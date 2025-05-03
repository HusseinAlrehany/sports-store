package com.coding.fitness;

import com.coding.fitness.dtos.UserDTO;
import com.coding.fitness.entity.User;
import com.coding.fitness.mapper.UserMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.*;


@DisplayName("Testing UserMapper Class")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserMapperTest {

    UserMapper userMapper;
    @BeforeAll
    void setUp(){
       userMapper = new UserMapper();
    }
    @Test
    @DisplayName("should map DTO to User Entity with Null password")
    void toEntityTest(){
        //given expected
        UserDTO dto = new UserDTO(1L, "hussein", "hus@gmail.com", null);
        //then
        User user = userMapper.toEntity(dto);
        //when
        assertEquals(dto.getId(), user.getId());
        assertEquals(dto.getName(), user.getName());
        assertEquals(dto.getEmail(), user.getEmail());
        assertNull(dto.getPassword(), "Password mapping should be ignored");

    }

    @Test
    @DisplayName("should map User Entity to DTO With Null Password ")
    void toDTOTestWithNullPassword(){
        //given expected
        User user = new User(1L,"al@test.com" ,null,"ali", null, null);

        //then
        UserDTO userDTO = userMapper.toDTO(user);

        //when
        assertEquals(user.getId(), userDTO.getId());
        assertEquals(user.getName(), userDTO.getName());
        assertEquals(user.getEmail(), userDTO.getEmail());
        assertNull(user.getPassword());
    }

    @Test
    @DisplayName("should throw null pointer exception if dto is null")
    void toEntityWithNullDTO(){

      var exp = assertThrows(NullPointerException.class, ()->
                       userMapper.toEntity(null));
      //to test also that the exact same message thrown in the userMapper
      assertEquals("UserDTO is Null", exp.getMessage());

    }

    @Test
    @DisplayName("should throw null pointer exception if user is null")
    void toDTOWithNullEntity(){
        var ex = assertThrows(NullPointerException.class, ()->
               userMapper.toDTO(null));
        assertEquals("User is Null", ex.getMessage());
    }

}

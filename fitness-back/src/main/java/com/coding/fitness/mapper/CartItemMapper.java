package com.coding.fitness.mapper;

import com.coding.fitness.dtos.CartItemsDTO;
import com.coding.fitness.entity.CartItems;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CartItemMapper {

    public CartItemsDTO toDTO(CartItems cartItems){
        CartItemsDTO cartItemsDTO = new CartItemsDTO();
        cartItemsDTO.setId(cartItems.getId());
        cartItemsDTO.setQuantity(cartItems.getQuantity());
        cartItemsDTO.setPrice(cartItems.getPrice());
        cartItemsDTO.setProductName(cartItems.getProduct().getName());
        cartItemsDTO.setReturnedImg(cartItems.getProduct().getImg());
        cartItemsDTO.setProductId(cartItems.getProduct().getId());
        cartItemsDTO.setOrderStatus(cartItems.getOrderStatus());


        return cartItemsDTO;
    }

    public List<CartItemsDTO> tocartItemsDTOSList(List<CartItems> cartItems){
        return cartItems != null ? cartItems.stream()
                        .map(this::toDTO)
                        .toList() : new ArrayList<>();
    }

}

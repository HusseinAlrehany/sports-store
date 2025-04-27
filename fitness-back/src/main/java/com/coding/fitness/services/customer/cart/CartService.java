package com.coding.fitness.services.customer.cart;

import com.coding.fitness.dtos.*;
import com.coding.fitness.entity.CartItems;
import com.coding.fitness.entity.Coupon;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CartService {

    CartItemsDTO addProductToCart(AddProductInCartDTO addProductInCartDTO);

    List<CartItemsDTO> getCartByUserId(Long userId);

    void clearCart(Long userId);

    void deleteCartItemById(Long itemId);

    CartItemsDTO increaseItemQuantity(AddProductInCartDTO addProductInCartDTO);

    CartItemsDTO decreaseItemQuantity(AddProductInCartDTO addProductInCartDTO);

}

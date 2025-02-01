package com.coding.fitness.controller.customer;

import com.coding.fitness.dtos.*;
import com.coding.fitness.exceptions.ValidationException;
import com.coding.fitness.services.customer.cart.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/customer")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping("/cart")
    public ResponseEntity<Map<String,String>> addProductToCart(@RequestBody AddProductInCartDTO addProductInCartDTO){
           boolean isSuccess = cartService.addProductToCart(addProductInCartDTO) != null;
           Map<String, String> response = new HashMap<>();
           if(isSuccess){
               response.put("message", "Product added to the cart successfully");
               return ResponseEntity.status(HttpStatus.OK).body(response);
           } else {
               response.put("message", "Failed to add product to the cart");
               return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
           }
    }

    @GetMapping("/cart/{userId}")
    public ResponseEntity<?>getCartByUserId(@PathVariable Long userId){

        return ResponseEntity.ok(cartService.getCartByUserId(userId));
    }

    @PostMapping("/placeOrder")
    public ResponseEntity<OrderDTO>placeOrder(@RequestBody PlaceOrderDTO orderDTO){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(cartService.placeOrder(orderDTO));
    }
    @DeleteMapping("/cart/{userId}")
    public ResponseEntity<Void> clearCart(@PathVariable Long userId){
           cartService.clearCart(userId);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
    @DeleteMapping("/cart-item/{cartItemId}")
    public ResponseEntity<Void> deleteCartItemById(@PathVariable Long cartItemId){
        cartService.deleteCartItemById(cartItemId);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
    @GetMapping("/order-summary/{userId}")
    public ResponseEntity<OrderSummary> getOrderSummary(@PathVariable Long userId){
        return ResponseEntity.status(HttpStatus.OK)
                .body(cartService.getOrderSummary(userId));
    }

    @PostMapping("/increase-quantity")
    public ResponseEntity<CartItemsDTO> increaseCartQuantity(@RequestBody AddProductInCartDTO addProductInCartDTO){

        return ResponseEntity.status(HttpStatus.OK)
                .body(cartService.increaseItemQuantity(addProductInCartDTO));
    }
    @PostMapping("/decrease-quantity")
    public ResponseEntity<CartItemsDTO> decreaseCartQuantity(@RequestBody AddProductInCartDTO addProductInCartDTO){
        return ResponseEntity.status(HttpStatus.OK)
                .body(cartService.decreaseItemQuantity(addProductInCartDTO));
    }

    @GetMapping("/coupon/{userId}/{code}")
    public ResponseEntity<OrderSummary> applyCoupon(@PathVariable Long userId, @PathVariable String code){

        return ResponseEntity.ok(cartService.applyCoupon(userId, code));
    }

    @GetMapping("/myOrders/{userId}")
    public ResponseEntity<List<OrderDTO>> getAllMyPlacedOrders(@PathVariable Long userId){

        return ResponseEntity.ok(cartService.findAllMyPlacedOrders(userId));
    }

}

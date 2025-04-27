package com.coding.fitness.services.customer.cart;
import com.coding.fitness.dtos.*;
import com.coding.fitness.entity.*;
import com.coding.fitness.enums.OrderStatus;
import com.coding.fitness.exceptions.ValidationException;
import com.coding.fitness.mapper.CartItemMapper;
import com.coding.fitness.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CartServiceImpl implements CartService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CartItemMapper cartItemMapper;

    private static final int FIXED_SHIPPING_COST = 20;

    @Override
    public CartItemsDTO addProductToCart(AddProductInCartDTO addProductInCartDTO) {
       User user = userRepository.findById(addProductInCartDTO.getUserId())
               .orElseThrow(()-> new ValidationException("User Not Found"));
       Product product = productRepository.findById(addProductInCartDTO.getProductId())
               .orElseThrow(()-> new ValidationException("Product Not Found"));

        //check if the product is already in the cart
        List<CartItems> cartItemsList = cartItemRepository.findByUserIdAndOrderIsNull(addProductInCartDTO.getUserId());

        Optional<CartItems> existingCartItems = cartItemsList
                .stream()
                .filter(cartItem -> cartItem.getProduct().getId().equals(addProductInCartDTO.getProductId()))
                .findFirst();

        //if the product is already in the cart, increase the quantity
        if(existingCartItems.isPresent()){
            return increaseItemQuantity(addProductInCartDTO);
        } else {

            //if not, create a new cart Item , means new record in database
            CartItems newCartItem = new CartItems();
            newCartItem.setProduct(product);
            newCartItem.setUser(user);
            newCartItem.setQuantity(1L);
            newCartItem.setPrice(product.getPrice());
            newCartItem.setOrderStatus(OrderStatus.PENDING);

          return cartItemMapper.toDTO(cartItemRepository.save(newCartItem)) ;
        }

    }

    @Override
    public List<CartItemsDTO> getCartByUserId(Long userId) {
        List<CartItems> cartItems = cartItemRepository.findByUserIdAndOrderIsNull(userId);
        if(cartItems.isEmpty()){
            throw new ValidationException("No Items Added to The Cart");
        }
        return cartItemMapper.tocartItemsDTOSList(cartItems);

    }


    @Override
    public void clearCart(Long userId){
          List<CartItems> cartItems = cartItemRepository.findByUserIdAndOrderIsNull(userId);

          if(cartItems.isEmpty()){
              throw new ValidationException("No Items Found!");
          }
          cartItemRepository.deleteAll(cartItems);

    }

    @Override
    public void deleteCartItemById(Long itemId) {
        Optional<CartItems> cartItems = cartItemRepository.findById(itemId);

        if(cartItems.isEmpty()){
            throw new ValidationException("Item not Found!");
        }
            cartItemRepository.delete(cartItems.get());
        }

    @Override
    public CartItemsDTO increaseItemQuantity(AddProductInCartDTO addProductInCartDTO) {

        CartItems cartItems = cartItemRepository.findByUserIdAndProductIdAndOrderIsNull(
                addProductInCartDTO.getUserId(),
                addProductInCartDTO.getProductId());
       if(cartItems == null ){
           throw new ValidationException("Cart Is Empty");
       }
        cartItems.setQuantity(cartItems.getQuantity() + 1);
        CartItems savedCartItems = cartItemRepository.save(cartItems);
        return cartItemMapper.toDTO(savedCartItems);
    }

    @Override
    public CartItemsDTO decreaseItemQuantity(AddProductInCartDTO addProductInCartDTO) {
        CartItems cartItems = cartItemRepository.findByUserIdAndProductIdAndOrderIsNull(
                addProductInCartDTO.getUserId(),
                addProductInCartDTO.getProductId());

        if(cartItems == null){
            throw new ValidationException("Cart Is Empty");
        }
              cartItems.setQuantity(cartItems.getQuantity() - 1);
              CartItems savedCartItems = cartItemRepository.save(cartItems);
        return cartItemMapper.toDTO(savedCartItems);
    }

}




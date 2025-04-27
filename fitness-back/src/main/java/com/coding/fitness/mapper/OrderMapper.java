package com.coding.fitness.mapper;

import com.coding.fitness.dtos.CartItemsDTO;
import com.coding.fitness.dtos.OrderDTO;
import com.coding.fitness.entity.Order;
import com.coding.fitness.enums.OrderStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class OrderMapper {


    private final CartItemMapper cartItemMapper;

    public OrderDTO toDTO(Order order){
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setId(order.getId());
        orderDTO.setOrderDescription(order.getOrderDescription());
        orderDTO.setOrderStatus(order.getOrderStatus());
        orderDTO.setAmount(order.getAmount());
        orderDTO.setTotalAmount(order.getTotalAmount());
        orderDTO.setDate(order.getDate());
        orderDTO.setAddress(order.getAddress());
        orderDTO.setTrackingId(order.getTrackingId());
        orderDTO.setUserName(order.getUser().getName());
        orderDTO.setUserId(order.getUser().getId());
        //setting the discount only if the coupon not null , else it will be null
        orderDTO.setDiscount(order.getCoupon()!= null ? order.getCoupon().getDiscount() : null);

        if(order.getCartItems() != null){
            List<CartItemsDTO> cartItemsDTOList = order.getCartItems()
                    .stream()
                    .map(cartItemMapper::toDTO)
                    .toList();
            orderDTO.setCartItems(cartItemsDTOList);
        }

        return orderDTO;

    }

    public List<OrderDTO> toDTOList(List<Order> orders){

        return orders != null ?
                orders.stream().map(this::toDTO).toList()
                : new ArrayList<>();
    }

    public void updateOrder(Order order, OrderDTO orderDTO){

        order.setOrderStatus(OrderStatus.PLACED);
        order.setOrderDescription(orderDTO.getOrderDescription());
        order.setDate(new Date());
        order.setTrackingId(UUID.randomUUID());
        order.setAddress(orderDTO.getAddress());
    }

}

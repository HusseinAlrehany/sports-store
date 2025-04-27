package com.coding.fitness.services.customer.customerorders;

import com.coding.fitness.dtos.OrderDTO;
import com.coding.fitness.dtos.OrderSummary;
import com.coding.fitness.dtos.PlaceOrderDTO;

import java.util.List;
import java.util.UUID;

public interface CustomerOrderService {

    OrderDTO placeOrder(PlaceOrderDTO orderDTO);

    OrderSummary getOrderSummary(Long userId);

    List<OrderDTO> findAllMyPlacedOrders(Long userId);

    OrderSummary applyCoupon(Long userId, String code);

    OrderDTO searchOrderByTrackingId(UUID trackingId);


}

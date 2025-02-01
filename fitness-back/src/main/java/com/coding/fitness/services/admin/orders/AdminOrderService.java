package com.coding.fitness.services.admin.orders;

import com.coding.fitness.dtos.AnalyticsResponse;
import com.coding.fitness.dtos.OrderDTO;
import com.coding.fitness.entity.Order;

import java.util.List;

public interface AdminOrderService {
    List<OrderDTO> findAllOrders();

    OrderDTO updateOrder(OrderDTO orderDTO);

    boolean isOrderUpdateExpired(Order order);

    void deleteOrder(Long orderId);

    OrderDTO changeOrderStatus(Long orderId, String orderStatus);

    AnalyticsResponse calculateAnalytics();
}

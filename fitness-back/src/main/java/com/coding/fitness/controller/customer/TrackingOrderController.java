package com.coding.fitness.controller.customer;

import com.coding.fitness.dtos.OrderDTO;
import com.coding.fitness.services.customer.cart.CartService;
import com.coding.fitness.services.customer.customerorders.CustomerOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class TrackingOrderController {

    private final CustomerOrderService customerOrderService;

    @GetMapping("/orders/{trackingId}")
    public ResponseEntity<OrderDTO> searchOrderByTrackingId(@PathVariable UUID trackingId){
       OrderDTO orderDTO = customerOrderService.searchOrderByTrackingId(trackingId);

       return orderDTO == null ? ResponseEntity.notFound().build() :
               ResponseEntity.ok(orderDTO);
    }
}

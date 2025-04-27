package com.coding.fitness.enums;

import com.coding.fitness.exceptions.ValidationException;

import java.util.Arrays;

public enum OrderStatus {

    DELIVERED("Delivered"),
    PENDING("Pending"),
    PLACED("Placed"),
    SHIPPED("Shipped");


    private final String orderStatus;

     OrderStatus(String orderStatus){
        this.orderStatus = orderStatus;
    }

    public String getOrderStatus(){
         return orderStatus;
    }

    public static OrderStatus fromStringToOrderStatus(String value){
          return Arrays.stream(OrderStatus.values())
                  .filter(status-> status.getOrderStatus().equalsIgnoreCase(value))
                  .findFirst()
                  .orElseThrow(()-> new ValidationException("Invalid Order Status"));
    }
}
